package com.test.core.conf

import com.test.core.processor.EventPublisher
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * @author 费世程
 * @date 2020/6/4 15:21
 */
@Component
class Runner(private val eventPublisher: EventPublisher) : ApplicationRunner {

  override fun run(args: ApplicationArguments?) {
    eventPublisher.init()
    Runtime.getRuntime().addShutdownHook(Thread {
      eventPublisher.destroy()
    })
  }
}