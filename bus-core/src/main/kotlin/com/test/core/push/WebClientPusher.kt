package com.test.core.push

import com.test.common.enum.UriType
import com.test.common.pojo.InnerEventMessage
import com.test.common.pojo.SubscribeInfo
import com.test.core.pojo.ExecutePushResponse
import com.test.core.processor.FailPushProcessor
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2020/6/8 14:36
 */
abstract class WebClientPusher(uriType: UriType, failPushProcessor: FailPushProcessor)
  : Pusher(uriType, failPushProcessor) {

  override fun executePush(subscribeInfo: SubscribeInfo, eventMessage: InnerEventMessage): Mono<ExecutePushResponse> {
    val webClient = getWebClient()
    return webClient.post().uri(subscribeInfo.pushUri).body(BodyInserters.fromValue(eventMessage)).exchange().flatMap { clientResponse ->
      val httpStatus = clientResponse.statusCode()
      clientResponse.bodyToMono(String::class.java).map {
        ExecutePushResponse(httpStatus.value(), it)
      }
    }
  }

  abstract fun getWebClient(): WebClient

}