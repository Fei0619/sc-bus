package com.test.core.processor.impl

import com.google.common.cache.CacheBuilder
import com.test.core.processor.IdempotentProcessor
import com.test.core.properties.BusProperties
import org.apache.commons.lang.StringUtils
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.TimeUnit

/**
 * @author 费世程
 * @date 2020/6/9 14:10
 */
class CacheIdempotentProcessor(busProperties: BusProperties) : IdempotentProcessor {

  private val logger = LoggerFactory.getLogger(CacheIdempotentProcessor::class.java)
  private val cache = CacheBuilder.newBuilder()
      .maximumSize(1.shl(20))
      .expireAfterAccess(busProperties.idempotentExpireSecond, TimeUnit.SECONDS)
      .concurrencyLevel(Runtime.getRuntime().availableProcessors())
      .build<String, String>()

  override fun idempotent(idempotentKey: String, expireTimeMillis: Long): Mono<Boolean> {
    if (StringUtils.isBlank(idempotentKey)) {
      return true.toMono()
    }
    var expire = false
    cache.get(idempotentKey) {
      expire = true
      ""
    }
    if (!expire) {
      logger.debug("idempotentKey=$idempotentKey")
    }
    return expire.toMono()
  }

}