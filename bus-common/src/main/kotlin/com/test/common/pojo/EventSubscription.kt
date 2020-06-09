package com.test.common.pojo

import cn.sh.ideal.nj.share.common.json.toJsonString

/**
 * @author 费世程
 * @date 2020/6/8 22:21
 */
class EventSubscription {

  var eventCode: String = ""
  var eventName: String = ""
  var subDetails = ArrayList<SubscribeDetails>()

  override fun toString(): String = this.toJsonString()
}