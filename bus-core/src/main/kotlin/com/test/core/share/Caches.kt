package com.test.core.share

import com.test.common.entity.BusConfig
import com.test.common.pojo.SubscribeDetails
import com.test.common.pojo.SubscribeInfo
import com.test.core.pojo.EventSubscription
import com.test.core.repository.ServiceRepository
import com.test.core.repository.SubscribeRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @author 费世程
 * @date 2020/6/8 17:37
 */
@Component
class Caches(private val serviceRepository: ServiceRepository,
             private val subscribeRepository: SubscribeRepository) {


  fun refreshLocalCache() {
    val subscribeList = subscribeRepository.findAll()
    val localCache = LocalCache()

  }

  /**
   * 每十分钟定时刷新缓存
   */
  @Scheduled(initialDelay = 10 * 60 * 1000, fixedRate = 10 * 60 * 1000)
  fun scheduledRefreshLocalCache() {
    refreshLocalCache()
  }

  private class LocalCache {
    /**
     * 事件id -> 该事件的描述信息和订阅信息
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