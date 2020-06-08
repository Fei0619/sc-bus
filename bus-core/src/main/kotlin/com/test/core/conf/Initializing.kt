package com.test.core.conf

import com.test.core.share.Caches
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

/**
 * @author 费世程
 * @date 2020/6/8 17:34
 */
@Component
class Initializing(private val caches: Caches) : InitializingBean {

  override fun afterPropertiesSet() {
    caches.refreshLocalCache()
  }

}