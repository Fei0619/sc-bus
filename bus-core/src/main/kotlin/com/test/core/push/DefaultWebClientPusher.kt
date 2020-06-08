package com.test.core.push

import com.test.common.enum.UriType
import com.test.core.processor.FailPushProcessor
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

/**
 * @author 费世程
 * @date 2020/6/8 14:48
 */
@Component
class DefaultWebClientPusher(private val webClient: WebClient,
                             failPushProcessor: FailPushProcessor)
  : WebClientPusher(UriType.application_name,failPushProcessor) {
  override fun getWebClient(): WebClient {
    return webClient
  }
}