package com.test.core.processor

import com.test.common.Destroyable
import com.test.common.Initable
import com.test.common.pojo.PublishDetails
import com.test.sdk.Res
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2020/6/4 22:44
 */
interface EventPublisher : Initable, Destroyable {
  /**
   * 发布一个事件
   * @param details PublishDetails
   * @return
   * @author 费世程
   * @date 2020/6/4 22:52
   */
  fun publishOne(details: PublishDetails): Mono<Res>
}