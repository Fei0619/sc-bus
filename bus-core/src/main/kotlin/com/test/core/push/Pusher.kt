package com.test.core.push

import com.test.common.enum.UriType
import com.test.common.pojo.InnerEventMessage
import com.test.common.pojo.SubscribeInfo
import com.test.core.pojo.ExecutePushResponse
import com.test.core.processor.FailPushProcessor
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import javax.annotation.PostConstruct

/**
 * @author 费世程
 * @date 2020/6/5 14:25
 */
abstract class Pusher(private val uriType: UriType,
                      private val failPushProcessor: FailPushProcessor) {

  @PostConstruct
  private fun register(uriType: UriType, pusher: Pusher) {
    registerPusher(uriType, this)
  }

  companion object {
    private val logger = LoggerFactory.getLogger(Pusher::class.java)
    private val pushMap = HashMap<UriType, Pusher>()

    private fun registerPusher(uriType: UriType, pusher: Pusher) {
      if (pushMap[uriType] != null) {
        logger.debug("${uriType}冲突...")
      }
      pushMap[uriType] = pusher
    }

    fun push(subscribeInfo: SubscribeInfo, eventMessage: InnerEventMessage): Mono<Unit> {
      val uriType = subscribeInfo.uriType
      val pusher = pushMap[uriType]
      if (pusher == null) {
        logger.error("$uriType ---> 没有该地址类型对应的Pusher...")
        throw RuntimeException("$uriType ---> 没有该地址类型对应的Pusher...")
      }
      return pusher.doPush(subscribeInfo, eventMessage)
    }

  }

  protected fun doPush(subscribeInfo: SubscribeInfo, eventMessage: InnerEventMessage): Mono<Unit> {
    val pushTime = System.currentTimeMillis()
    return executePush(subscribeInfo, eventMessage).map { response ->
      response.pushTime = pushTime
      response.responseTime = System.currentTimeMillis()
      response
    }.map { pusherResponse ->
      if (pusherResponse.isSuccess()) {
        if (logger.isDebugEnabled) {
          logger.debug("${eventMessage.eventCode} --> 推送成功,耗时${System.currentTimeMillis() - pushTime}")
          Unit.toMono()
        }
      } else {
        if (logger.isDebugEnabled) {
          logger.debug("${eventMessage.eventCode} --> 推送失败,耗时${System.currentTimeMillis() - pushTime}")
          failPushProcessor.failProcessor(subscribeInfo, eventMessage)
        }
      }
    }
  }

  abstract fun executePush(subscribeInfo: SubscribeInfo, eventMessage: InnerEventMessage): Mono<ExecutePushResponse>

}