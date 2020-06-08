package com.test.common.pojo

import cn.sh.ideal.nj.share.common.json.toJsonString

/**
 * @author 费世程
 * @date 2020/6/5 11:33
 */
class SubscribeInfo : Cloneable {

  var subscribeId: Int? = null
  var serviceId: Int? = null
  var serviceName: String? = null
  var serviceDesc: String? = null
  var pushUri: String? = null
  var uriType: String? = null
  var retryCount: Int? = null

  public override fun clone(): SubscribeInfo {
    return super.clone() as SubscribeInfo
  }

  override fun toString(): String = this.toJsonString()
}