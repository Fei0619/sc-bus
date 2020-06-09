package com.test.core.processor.impl

import com.test.common.redis.ReactiveRedisClient
import com.test.core.processor.IdempotentProcessor
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2020/6/9 14:11
 */
class RedisIdempotentProcessor(private val reactiveRedisClient: ReactiveRedisClient) : IdempotentProcessor {

  private val prefix = "sc:bus:idempotent"

  override fun idempotent(idempotentKey: String, expireTimeMillis: Long): Mono<Boolean> {
    val key = "$prefix:$idempotentKey"
    return reactiveRedisClient.setIfAbsent(key, "", expireTimeMillis)
  }

}