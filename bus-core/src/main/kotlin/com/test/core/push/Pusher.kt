package com.test.core.push

import com.test.common.pojo.InnerEventMessage
import com.test.common.pojo.SubscribeInfo
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

/**
 * @author 费世程
 * @date 2020/6/5 14:25
 */
object Pusher {

  fun push(subscribeInfo: SubscribeInfo, eventMessage: InnerEventMessage): Mono<Unit> {
    return Unit.toMono()
  }

}