package com.test.core.conf

import com.test.core.processor.DelayProcessor
import com.test.core.processor.EventPublisher
import com.test.core.properties.BusProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author 费世程
 * @date 2020/6/4 23:12
 */
@Configuration
class DefaultBeanConfig(private val busProperties: BusProperties) {

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