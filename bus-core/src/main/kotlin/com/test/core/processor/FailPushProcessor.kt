package com.test.core.processor

import com.test.common.pojo.InnerEventMessage
import com.test.common.pojo.SubscribeInfo
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2020/6/8 16:21
 */
interface FailPushProcessor {

  fun failProcessor(subscribeInfo: SubscribeInfo, eventMessage: InnerEventMessage<*>): Mono<Unit>

}