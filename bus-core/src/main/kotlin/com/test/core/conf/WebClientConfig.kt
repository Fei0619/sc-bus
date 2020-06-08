package com.test.core.conf

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient

/**
 * @author 费世程
 * @date 2020/6/8 14:23
 */
@Configuration
class WebClientConfig {

  @Bean
  @LoadBalanced
  fun loadBalancedWebClient(reactiveClientHttpConnector: ReactorClientHttpConnector): WebClient.Builder {
    return WebClient.builder().clientConnector(reactiveClientHttpConnector)
  }

  /**
   * 用于外部服务
   */
  @Bean
  fun webClient(reactiveClientHttpConnector: ReactorClientHttpConnector): WebClient {
    return WebClient.builder().clientConnector(reactiveClientHttpConnector).build()
  }

}