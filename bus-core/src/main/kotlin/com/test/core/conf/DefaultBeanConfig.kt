package com.test.core.conf

import com.test.common.redis.ReactiveRedisClient
import com.test.core.processor.DelayProcessor
import com.test.core.processor.EventPublisher
import com.test.core.processor.FailPushProcessor
import com.test.core.processor.IdempotentProcessor
import com.test.core.processor.impl.CacheIdempotentProcessor
import com.test.core.processor.impl.DefaultFailPushProcessor
import com.test.core.processor.impl.RedisIdempotentProcessor
import com.test.core.properties.BusProperties
import com.test.core.properties.Module
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.ReactiveStringRedisTemplate

/**
 * @author 费世程
 * @date 2020/6/4 23:12
 */
@Configuration
class DefaultBeanConfig(private val busProperties: BusProperties) {

  @Bean
  fun reactiveRedisClient(template: ReactiveStringRedisTemplate): ReactiveRedisClient {
    return ReactiveRedisClient(template)
  }

  @Bean
  @ConditionalOnMissingBean
  fun idempotentProcessor(reactiveRedisClient: ReactiveRedisClient): IdempotentProcessor {
    return if (busProperties.modules.contains(Module.redis)) {
      RedisIdempotentProcessor(reactiveRedisClient)
    } else {
      CacheIdempotentProcessor(busProperties)
    }
  }

  @Bean
  @ConditionalOnMissingBean
  fun failPushProcessor(delayProcessor: DelayProcessor): FailPushProcessor {
    return DefaultFailPushProcessor(delayProcessor)
  }

  @Bean
  @ConditionalOnMissingBean
  fun delayProcessor(): DelayProcessor {
    TODO()
  }

  @Bean
  @ConditionalOnMissingBean
  fun eventPublisher(delayProcessor: DelayProcessor): EventPublisher {
    TODO()
  }

}