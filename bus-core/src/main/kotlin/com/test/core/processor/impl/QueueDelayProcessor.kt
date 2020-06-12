package com.test.core.processor.impl

import com.test.common.json.JsonUtils
import com.test.common.pojo.PublishDetails
import com.test.common.pojo.SubscribeInfo
import com.test.common.share.Commons
import com.test.core.pojo.DelayMessage
import com.test.core.pojo.FailPushRetryInfo
import com.test.core.processor.DelayProcessor
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
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

  override fun delay(publishDetails: PublishDetails): Mono<Unit> {
    TODO("not implemented")
  }

  override fun delay(failPushRetryInfo: FailPushRetryInfo): Mono<Unit> {
    TODO("not implemented")
  }

  /**
   * 推送延迟数据
   */
  private fun startPublish() {
    start=true
    consumeThread=Thread{
      while (start){
        queue.poll(60,TimeUnit.SECONDS)?.let { delayMessage ->
          Commons.threadPool().execute{

          }
        }
      }
    }
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