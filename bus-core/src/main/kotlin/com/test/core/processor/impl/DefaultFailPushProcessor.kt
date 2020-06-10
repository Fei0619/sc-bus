package com.test.core.processor.impl

import com.test.common.pojo.InnerEventMessage
import com.test.common.pojo.SubscribeInfo
import com.test.core.pojo.FailPushRetryInfo
import com.test.core.processor.DelayProcessor
import com.test.core.processor.FailPushProcessor
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2020/6/8 16:24
 */
class DefaultFailPushProcessor(private val delayProcessor: DelayProcessor)
  : FailPushProcessor {
  override fun failProcessor(subscribeInfo: SubscribeInfo, eventMessage: InnerEventMessage): Mono<Unit> {
    subscribeInfo.retryCount += 1
    val failPushRetryInfo = FailPushRetryInfo(subscribeInfo, eventMessage)
    return delayProcessor.delay(failPushRetryInfo)
  }
}