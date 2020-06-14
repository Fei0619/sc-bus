package com.test.core.processor.impl

import com.test.common.date.DateUtils
import com.test.common.json.JsonUtils
import com.test.common.pojo.PublishDetails
import com.test.common.pojo.SubscribeInfo
import com.test.common.share.Commons
import com.test.core.pojo.DelayMessage
import com.test.core.pojo.FailPushRetryInfo
import com.test.core.processor.DelayProcessor
import com.test.core.push.Pusher
import com.test.core.share.Caches
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.io.*
import java.util.concurrent.DelayQueue
import java.util.concurrent.TimeUnit

/**
 * @author 费世程
 * @date 2020/6/10 13:56
 */
class QueueDelayProcessor : DelayProcessor {

  private val logger = LoggerFactory.getLogger(QueueDelayProcessor::class.java)
  private val queue = DelayQueue<DelayMessage<*>>()

  private val fileName = "delay_publish_infos"
  private var consumeThread: Thread? = null

  @Volatile
  private var start = true

  override fun init() {
    readPersistenceMessage()
    startPublish()
  }

  override fun destroy() {
    persistenceMessage()
    start = false
  }

  /**
   * 延迟消息入延迟队列
   */
  override fun delay(publishDetails: PublishDetails): Mono<Unit> {
    val message = publishDetails.eventMessage ?: return Unit.toMono()
    val delayMillis = message.delayMillis
    val pushTime = message.eventTime + delayMillis
    if (logger.isDebugEnabled) {
      logger.debug("延迟消息 eventCode=${publishDetails.eventCode} 将于 $delayMillis ms后推送 -> ${DateUtils.parse(pushTime)}")
    }
    val delayMessage = DelayMessage(DelayMessage.MessageType.DELAY_MESSAGE, pushTime, message)
    queue.offer(delayMessage)
    return Unit.toMono()
  }

  /**
   * 失败消息入延迟队列
   */
  override fun delay(failPushRetryInfo: FailPushRetryInfo): Mono<Unit> {
    val subscribeInfo = failPushRetryInfo.subscribeInfo
    val message = failPushRetryInfo.eventMessage
    val retryCount = subscribeInfo.retryCount
    val maxPushCount = Caches.getMaxPushCount()
    if (retryCount > maxPushCount) {
      logger.debug("已达最大推送次数，丢弃该消息：${JsonUtils.toJsonString(message)}")
    }
    val firstPushTime = message.eventTime + message.delayMillis
    val nextPushTime = Caches.getNextPushTime(firstPushTime, retryCount)
    if (logger.isDebugEnabled) {
      logger.debug("接收推送失败消息：${JsonUtils.toJsonString(message)} ，将于 ${nextPushTime - System.currentTimeMillis()} ms后重试 -> ${DateUtils.parse(nextPushTime)}")
    }
    val delayMessage = DelayMessage(DelayMessage.MessageType.PUSH_FAIL_MESSAGE, nextPushTime, failPushRetryInfo)
    queue.offer(delayMessage)
    return Unit.toMono()
  }

  /**
   * 推送延迟数据
   */
  private fun startPublish() {
    start = true
    consumeThread = Thread {
      while (start) {
        queue.poll(60, TimeUnit.SECONDS)?.let { delayMessage ->
          Commons.threadPool().execute {
            when (delayMessage.messageType) {
              DelayMessage.MessageType.DELAY_MESSAGE -> {
                publishDelayMessage(delayMessage)
              }
              DelayMessage.MessageType.PUSH_FAIL_MESSAGE -> {
                publishFailMessage(delayMessage)
              }
            }
          }
        }
      }
    }
    consumeThread!!.start()
  }

  /**
   * 推送延迟消息
   */
  private fun publishDelayMessage(delayMessage: DelayMessage<*>) {
    logger.debug("从延迟队列中取得延迟消息推送...")
    val publishDetails = delayMessage.message as PublishDetails
    val message = publishDetails.eventMessage!!
    publishDetails.subscribes.toFlux().map {
      Pusher.push(it, message)
    }
  }

  /**
   * 推送失败消息
   */
  private fun publishFailMessage(delayMessage: DelayMessage<*>) {
    logger.debug("从延迟队列中取得推送失败的消息重新推送...")
    val failPushRetryInfo = delayMessage.message as FailPushRetryInfo
    val message = failPushRetryInfo.eventMessage
    val subscribeInfo = failPushRetryInfo.subscribeInfo
    Pusher.push(subscribeInfo, message).subscribe()
  }

  /**
   * 读取持久化消息
   */
  private fun readPersistenceMessage() {
    val file = File(fileName)
    if (file.exists()) {
      FileInputStream(file).use { fis ->
        InputStreamReader(fis).use { isr ->
          BufferedReader(isr).use { br ->
            val line = br.readLine()
            if (line.isNotBlank()) {
              when {
                line.startsWith("{\"messageType\":\"DELAY_MESSAGE\"") -> {
                  queue.offer(JsonUtils.parseJson(line, DelayMessage::class.java, SubscribeInfo::class.java))
                }
                line.startsWith("{\"messageType\":\"PUSH_FAIL_MESSAGE\"") -> {
                  queue.offer(JsonUtils.parseJson(line, DelayMessage::class.java, FailPushRetryInfo::class.java))
                }
                else -> logger.debug("$line 数据格式异常")
              }
            }
          }
        }
      }
    }
  }

  /**
   * 程序关闭时将未处理完的消息持久化到文件
   */
  private fun persistenceMessage() {
    if (queue.size > 0) {
      val array = queue.toArray()
      val file = File(fileName)
      FileOutputStream(file).use { fos ->
        OutputStreamWriter(fos).use { osw ->
          BufferedWriter(osw).use { bw ->
            array.forEach {
              bw.write(JsonUtils.toJsonString(it))
              bw.newLine()
            }
            bw.flush()
            bw.close()
          }
        }
      }
    }
  }
}