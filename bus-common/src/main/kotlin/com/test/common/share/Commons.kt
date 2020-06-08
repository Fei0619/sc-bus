package com.test.common.share

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.*

/**
 * @author 费世程
 * @date 2020/6/5 14:35
 */
object Commons {
  val size = Runtime.getRuntime().availableProcessors().shl(1)

  fun threadPool(): ThreadPoolExecutor {
    return ThreadPoolExecutor(0, size, 60L, TimeUnit.SECONDS,
        ArrayBlockingQueue(1.shl(10)), ThreadFactoryBuilder().setNameFormat("common-pool-%d").build())
  }

}