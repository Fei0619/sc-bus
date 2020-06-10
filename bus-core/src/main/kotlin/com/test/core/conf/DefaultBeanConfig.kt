package com.test.core.conf

import com.test.common.redis.ReactiveRedisClient
import com.test.core.processor.DelayProcessor
import com.test.core.processor.EventPublisher
import com.test.core.processor.FailPushProcessor
import com.test.core.processor.IdempotentProcessor
import com.test.core.processor.impl.*
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

  /**
   * 幂等
   */
  @Bean
  @ConditionalOnMissingBean
  fun idempotentProcessor(reactiveRedisClient: ReactiveRedisClient): IdempotentProcessor {
    return if (busProperties.modules.contains(Module.redis)) {
      RedisIdempotentProcessor(reactiveRedisClient)
    } else {
      CacheIdempotentProcessor(busProperties)
    }
  }

  /**
   * 失败处理器
   */
  @Bean
  @ConditionalOnMissingBean
  fun failPushProcessor(delayProcessor: DelayProcessor): FailPushProcessor {
    return DefaultFailPushProcessor(delayProcessor)
  }

  /**
   * 延迟处理器
   */
  @Bean
  @ConditionalOnMissingBean
  fun delayProcessor(): DelayProcessor {
    if (busProperties.modules.contains(Module.rabbitmq)) {
      return RabbitDelayProcessor()
    } else if (busProperties.modules.contains(Module.kafka)) {
      return KafkaDelayProcessor()
    }
    return QueueDelayProcessor()
  }

  /**
   * 消息发布
   */
  @Bean
  @ConditionalOnMissingBean
  fun eventPublisher(delayProcessor: DelayProcessor): EventPublisher {
    return AsyncEventPublisher(delayProcessor)
  }

}