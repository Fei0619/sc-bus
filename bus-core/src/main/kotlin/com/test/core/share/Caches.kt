package com.test.core.share

import com.test.core.repository.ServiceRepository
import com.test.core.repository.SubscribeRepository
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
  }

  private class LocalCache(){

  }

}