package com.test.core.share

import com.test.common.entity.BusConfig
import com.test.common.pojo.SubscribeDetails
import com.test.common.pojo.EventSubscription
import com.test.common.pojo.SubscribeInfo
import com.test.dao.BusEventDao
import com.test.dao.repository.EventRepository
import com.test.dao.repository.ServiceRepository
import com.test.dao.repository.SubscribeRepository
import org.apache.commons.lang.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @author 费世程
 * @date 2020/6/8 17:37
 */
@Component
class Caches(private val busEventDao: BusEventDao) {

  companion object {
    private val logger = LoggerFactory.getLogger(Caches::class.java)
    private lateinit var localCache: LocalCache

    fun getSubscribes(eventCode: String): EventSubscription? {
      return localCache.eventSubscriptionMapper[eventCode]
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
    localCache.eventSubscriptionMapper = eventSubList.associateBy { it.eventCode }
    localCache.subscribeDetailsMapper = eventSubList.flatMap { it.subDetails }.associateBy { it.subscribeId }
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