package com.test.core.processor.impl

import com.test.common.pojo.PublishDetails
import com.test.core.pojo.FailPushRetryInfo
import com.test.core.processor.DelayProcessor
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2020/6/5 10:30
 */
class KafkaDelayProcessor : DelayProcessor {
  override fun delay(publishDetails: PublishDetails): Mono<Unit> {
    TODO("not implemented")
  }

  override fun delay(failPushRetryInfo: FailPushRetryInfo): Mono<Unit> {
    TODO("not implemented")
  }

  override fun init() {
    TODO("not implemented")
  }

  override fun destroy() {
    TODO("not implemented")
  }
}