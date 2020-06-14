package com.test.core.share

import com.test.common.entity.BusConfig
import com.test.common.pojo.SubscribeDetails
import com.test.common.pojo.EventSubscription
import com.test.dao.BusEventDao
import com.test.dao.repository.ConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @author 费世程
 * @date 2020/6/8 17:37
 */
@Component
class Caches(private val busEventDao: BusEventDao,
             private val configRepository: ConfigRepository) {

  companion object {
    private val logger = LoggerFactory.getLogger(Caches::class.java)
    private lateinit var localCache: LocalCache

    /**
     * 获取事件订阅者信息
     * @param eventCode 事件码值
     */
    fun getSubscribes(eventCode: String): EventSubscription? {
      return localCache.eventSubscriptionMapper[eventCode]
    }

    /**
     * 获取最大推送次数
     */
    fun getMaxPushCount(): Int {
      val maxCount = localCache.configMapper["maxPushCount"] ?: return 1
      return maxCount.propertyValue!!.toInt()
    }

    /**
     * 获取失败重试时间间隔
     */
    private fun getRetryTimeInterval(): Long {
      val timeInterval = localCache.configMapper["failRetryTimeInterval"] ?: return 1
      return timeInterval.propertyValue!!.toLong()
    }

    /**
     * 获取下次重试时间
     * @param firstPushTime 首次推送时间
     * @param num 第几次重试
     */
    fun getNextPushTime(firstPushTime: Long, num: Int): Long {
      val timeInterval = getRetryTimeInterval()
      var increment = 0L
      for (i in 1 until num + 1) {
        increment += timeInterval * i
        System.err.println("increment=$increment")
      }
      return firstPushTime + increment
    }

  }

  /**
   * 每十分钟定时刷新缓存
   */
  @Scheduled(initialDelay = 10 * 60 * 1000, fixedRate = 10 * 60 * 1000)
  fun scheduledRefreshLocalCache() {
    refreshLocalCache()
  }

  fun refreshLocalCache() {
    logger.debug("刷新本地缓存...")
    val localCache = LocalCache()
    val eventSubList = busEventDao.listAllEventSubscription()
    val configList = configRepository.findAll()
    localCache.eventSubscriptionMapper = eventSubList.associateBy { it.eventCode }
    localCache.subscribeDetailsMapper = eventSubList.flatMap { it.subDetails }.associateBy { it.subscribeId }
    localCache.configMapper = configList.associateBy { it.property!! }
  }

  private class LocalCache {
    /**
     * 事件编码 -> 该事件的描述信息和订阅信息
     */
    lateinit var eventSubscriptionMapper: Map<String, EventSubscription>
    /**
     * 订阅id -> 订阅详情
     */
    lateinit var subscribeDetailsMapper: Map<Int, SubscribeDetails>
    /**
     * 服务配置信息：属性名 -> 属性配置
     */
    lateinit var configMapper: Map<String, BusConfig>
  }

}