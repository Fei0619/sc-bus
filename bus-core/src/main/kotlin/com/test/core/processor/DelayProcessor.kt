package com.test.core.processor

import com.test.common.Destroyable
import com.test.common.Initable
import com.test.common.pojo.InnerEventMessage
import com.test.common.pojo.PublishDetails
import com.test.common.pojo.SubscribeInfo
import com.test.core.pojo.FailPushRetryInfo
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2020/6/5 9:46
 */
interface DelayProcessor : Initable, Destroyable {

  /**
   * 延迟推送
   * @param publishDetails PublishDetails
   * @return
   * @author 费世程
   * @date 2020/6/5 9:47
   */
  fun delay(publishDetails: PublishDetails): Mono<Unit>

  fun delay(failPushRetryInfo: FailPushRetryInfo): Mono<Unit>

}