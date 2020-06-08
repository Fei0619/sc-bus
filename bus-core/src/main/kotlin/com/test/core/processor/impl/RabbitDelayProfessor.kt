package com.test.core.processor.impl

import com.test.common.pojo.PublishDetails
import com.test.core.processor.DelayProcessor
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

/**
 * @author 费世程
 * @date 2020/6/5 10:25
 */
class RabbitDelayProfessor : DelayProcessor {

  override fun delay(publishDetails: PublishDetails): Mono<Unit> {
    return Unit.toMono()
  }

  override fun init() {
  }

  override fun destroy() {
  }
}