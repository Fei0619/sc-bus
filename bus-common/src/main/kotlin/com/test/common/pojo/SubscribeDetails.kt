package com.test.common.pojo

import cn.sh.ideal.nj.share.common.json.toJsonString
import com.test.common.entity.BusService
import java.util.regex.Pattern

/**
 * @author 费世程
 * @date 2020/6/8 22:18
 */
class SubscribeDetails : BusService() {

  var subscribeId: Int = -1
  var eventCode: String? = null
  var conditions: String = ""
  var isBroadcast: Int = 0
  var subscribeDesc: String? = null

  fun getConditions(): Set<String> {
    return conditions.split(";").toHashSet()
  }

  fun constructSubscribeInfo(): SubscribeInfo {
    val subscribeInfo = SubscribeInfo()
    subscribeInfo.subscribeId = subscribeId
    subscribeInfo.serviceId = serviceId
    subscribeInfo.serviceName = serviceName
    subscribeInfo.serviceDesc = serviceDesc
    if (pushUri != null) {
      subscribeInfo.pushUri = pushUri!!
    }
    if (uriType != null) {
      subscribeInfo.uriType = uriType!!
    }
    subscribeInfo.conditions = conditions
    return subscribeInfo
  }


  override fun toString(): String = this.toJsonString()

}