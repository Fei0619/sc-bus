package com.test.core.pojo

import com.test.common.pojo.InnerEventMessage
import com.test.common.pojo.SubscribeInfo

/**
 * @author 费世程
 * @date 2020/6/10 14:22
 */
class FailPushRetryInfo(var subscribeInfo: SubscribeInfo, var eventMessage: InnerEventMessage<*>)