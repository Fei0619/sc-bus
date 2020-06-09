package com.test

import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit

/**
 * @author 费世程
 * @date 2020/6/9 14:47
 */
class CacheTest

fun main() {
  val cache = CacheBuilder.newBuilder()
      .maximumSize(1.shl(20))
      .expireAfterAccess(5, TimeUnit.SECONDS)
      .concurrencyLevel(Runtime.getRuntime().availableProcessors())
      .build<String, String>()
  var flag = false
  System.err.println("initial time --> $flag")
  System.err.println("initial time --> ${cache.getIfPresent("key")}")
  System.err.println("-----------")
  cache.get("key") {
    flag = true
    "value1"
  }
  System.err.println("first time --> $flag")
  System.err.println("first time --> ${cache.getIfPresent("key")}")
  System.err.println("-----------")
  cache.get("key") {
    flag = true
    "value2"
  }
  System.err.println("second time --> $flag")
  System.err.println("second time --> ${cache.getIfPresent("key")}")

  TimeUnit.SECONDS.sleep(5)
  System.err.println("after sleep 5 second --> ${cache.getIfPresent("key")}")

}