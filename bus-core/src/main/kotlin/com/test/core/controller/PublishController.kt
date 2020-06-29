package com.test.core.controller

import com.test.common.enum.UriType
import com.test.common.json.JsonUtils
import com.test.common.pojo.InnerEventMessage
import com.test.common.pojo.PublishDetails
import com.test.common.pojo.SubscribeInfo
import com.test.core.processor.EventPublisher
import com.test.core.processor.IdempotentProcessor
import com.test.core.properties.BusProperties
import com.test.core.share.Caches
import com.test.sdk.Res
import org.apache.commons.lang.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.cloud.netflix.ribbon.SpringClientFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import javax.validation.Valid

/**
 * @author 费世程
 * @date 2020/6/8 16:45
 */
@RequestMapping("/publish")
@RestController
class PublishController(private val eventPublisher: EventPublisher,
                        private val idempotentProcessor: IdempotentProcessor,
                        private val springClientFactory: SpringClientFactory,
                        private val busProperties: BusProperties) {

  private val logger = LoggerFactory.getLogger(PublishController::class.java)


  @PostMapping("/one")
  fun publishOne(@RequestBody @Valid message: InnerEventMessage<*>): Mono<Res> {
    var startTimeMillis: Long? = null
    if (logger.isDebugEnabled) {
      startTimeMillis = System.currentTimeMillis()
    }
    val idempotentExpireSecond = busProperties.idempotentExpireSecond
    return idempotentProcessor.idempotent(message.idempotentKey, idempotentExpireSecond).flatMap {
      if (it) {
        val actualSubList = getActualSubscribes(message)
        if (actualSubList.isEmpty()) {
          logger.info("没有订阅此事件的服务 --> eventCode=${message.eventCode}")
          Res.ok("没有订阅此事件的服务 --> eventCode=${message.eventCode}").toMono()
        } else {
          val publishDetails = PublishDetails(message, actualSubList)
          eventPublisher.publishOne(publishDetails)
        }
      } else {
        logger.info("此事件在 $idempotentExpireSecond 期间重复，已被丢弃...")
        Res.ok("此事件在 $idempotentExpireSecond 期间重复，已被丢弃...").toMono()
      }
    }.doFinally {
      if (startTimeMillis != null) {
        val timeConsume = System.currentTimeMillis() - startTimeMillis
        logger.debug("eventCode=${message.eventCode}事件推送完成，耗时：$timeConsume ms")
      }
    }
  }

  @PostMapping
  fun batchPublish(@RequestBody @Valid messages: List<InnerEventMessage<*>>): Mono<Res> {
    var startTimeMillis: Long? = null
    if (logger.isDebugEnabled) {
      startTimeMillis = System.currentTimeMillis()
    }
    return messages.toFlux().map { publishOne(it) }.collectList().map { Res.ok() }.doFinally {
      if (startTimeMillis != null) {
        val timeConsuming = System.currentTimeMillis() - startTimeMillis
        val eventIds = messages.map { it.eventId }.reduce { acc, s -> acc + (if (s == "") "" else ", ") + s }
        logger.debug("批量推送完成，eventIds=$eventIds，总耗时=$timeConsuming ms")
      }
    }
  }

  /**
   * 获取事件的实际订阅者
   */
  fun getActualSubscribes(message: InnerEventMessage<*>): List<SubscribeInfo> {
    val eventCode = message.eventCode
    if (StringUtils.isBlank(eventCode)) {
      return emptyList()
    }
    val eventSubscription = Caches.getSubscribes(eventCode)
    if (eventSubscription == null) {
      logger.debug("没有此事件的订阅信息 --> eventCode=${eventCode}")
      return emptyList()
    }
    val subList = eventSubscription.subDetails
    if (subList.isEmpty()) {
      logger.debug("没有此事件的订阅信息 --> eventCode=${eventCode}")
      return emptyList()
    }
    val actualSubList = ArrayList<SubscribeInfo>()
    subList.filter {
      val conditions = it.getConditionGroup()
      val match = message.matches(conditions)
      if (!match && logger.isDebugEnabled) {
        logger.debug("subscribeId=${it.subscribeId} 的订阅条件：${it.conditions}与事件推送条件：${JsonUtils.toJsonString(conditions)}不匹配...")
      }
      match
    }.forEach { subscription ->
      val subscribeInfo = subscription.constructSubscribeInfo()
      if (subscription.isBroadcast == 0) {
        actualSubList.add(subscribeInfo)
      } else {
        if (subscription.uriType == UriType.application_name) {
          actualSubList.add(subscribeInfo)
          logger.info("uriType= ${subscription.uriType} 不支持广播模式...")
        } else {
          val split = StringUtils.split(subscription.pushUri, "/", 3)
          val loadBalancer = springClientFactory.getLoadBalancer(split[1])
          if (loadBalancer != null) {
            val servers = loadBalancer.allServers
            for (server in servers) {
              val clone = subscribeInfo.clone()
              clone.pushUri = "${split[0]}//${server.host}:${server.port}/${split[2]}"
              actualSubList.add(clone)
            }
          }
        }
      }
    }
    return emptyList()
  }


}