package com.test.core.pojo

import cn.sh.ideal.nj.share.common.json.toJsonString
import com.test.common.pojo.SubscribeDetails

/**
 * @author 费世程
 * @date 2020/6/8 22:21
 */
class EventSubscription {

  var eventCode: String = ""
  var eventName: String = ""
  var subDetails: List<SubscribeDetails> = ArrayList()

  override fun toString(): String = this.toJsonString()
}