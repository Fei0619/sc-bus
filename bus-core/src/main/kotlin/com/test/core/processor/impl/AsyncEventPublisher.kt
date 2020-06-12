package com.test.core.processor.impl

import com.test.common.json.JsonUtils
import com.test.common.pojo.PublishDetails
import com.test.common.share.Commons
import com.test.core.processor.DelayProcessor
import com.test.core.processor.EventPublisher
import com.test.core.push.Pusher
import com.test.sdk.Res
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.io.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

/**
 * 异步推送
 * @author 费世程
 * @date 2020/6/4 22:45
 */
class AsyncEventPublisher(private val delayProcessor: DelayProcessor) : EventPublisher {

  private val logger = LoggerFactory.getLogger(AsyncEventPublisher::class.java)
  private val queue = ArrayBlockingQueue<PublishDetails>(1.shl(14))

  private val fileName = "async_publish_details"
  private var consumptionThread: Thread? = null

  @Volatile
  private var start = true

  override fun publishOne(details: PublishDetails): Mono<Res> {
    val offer = queue.offer(details)
    return if (offer) {
      Res.ok().toMono()
    } else {
      Res.error("消息放入队列失败...").toMono()
    }
  }

  private fun startPublish() {
    start = true
    consumptionThread = Thread {
      while (start) {
        queue.poll(10, TimeUnit.SECONDS)?.let { publishDetails ->
          Commons.threadPool().execute {
            if (publishDetails.needDelay) {
              logger.debug("延时消息...")
              delayProcessor.delay(publishDetails).map { Res.ok() }.subscribe()
            } else {
              logger.debug("推送消息...")
              val eventMessage = publishDetails.eventMessage!!
              publishDetails.subscribes.toFlux().flatMap { Pusher.push(it, eventMessage) }.subscribe()
            }
          }
        }
      }
    }
    consumptionThread!!.start()
  }

  override fun init() {
    readPersistenceMessage()
    startPublish()
  }

  override fun destroy() {
    persistenceMessage()
    start = false
  }

  /**
   * 读取持久化文件
   */
  private fun readPersistenceMessage() {
    val file = File(fileName)
    if (file.exists()) {
      file.inputStream().use { ips ->
        ips.bufferedReader().useLines { line ->
          val details = JsonUtils.parseJson(line.toString(), PublishDetails::class.java)
          queue.put(details)
        }
      }
      file.delete()
    }
  }

  /**
   * 程序关闭时将还未处理完的消息写入文件
   */
  private fun persistenceMessage() {
    if (queue.size > 0) {
      val array = queue.toArray()
      val file = File(fileName)
      FileOutputStream(file).use { fos ->
        OutputStreamWriter(fos).use { osw ->
          BufferedWriter(osw).use { bfd ->
            array.forEach {
              bfd.write(JsonUtils.toJsonString(it))
              bfd.newLine()
            }
            bfd.flush()
            bfd.close()
          }
        }
      }
    }
  }

}