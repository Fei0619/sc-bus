package com.test.common.redis

import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import reactor.core.publisher.Mono
import java.time.Duration

/**
 * @author 费世程
 * @date 2020/6/9 13:59
 */
class ReactiveRedisClient(private val template: ReactiveStringRedisTemplate) {

  fun get(key: String): Mono<String> {
    return template.opsForValue().get(key)
  }

  @JvmOverloads
  fun setIfAbsent(key: String, value: String = "", expireTime: Long?): Mono<Boolean> {
    return if (expireTime == null) {
      template.opsForValue().setIfAbsent(key, value)
    } else {
      template.opsForValue().setIfPresent(key, value, Duration.ofSeconds(expireTime))
    }
  }

}