package com.test.common.pojo

import cn.sh.ideal.nj.share.common.json.toJsonString
import com.test.common.enum.UriType

/**
 * @author 费世程
 * @date 2020/6/5 11:33
 */
class SubscribeInfo : Cloneable {

  var subscribeId: Int? = null
  /**
   * 服务id
   */
  var serviceId: Int? = null
  /**
   * 服务名称
   */
  var serviceName: String? = null
  /**
   * 服务描述
   */
  var serviceDesc: String? = null
  /**
   * 推送地址
   */
  var pushUri: String = ""
  /**
   *地址类型
   */
  var uriType: UriType = UriType.actual_address
  /**
   * 订阅条件
   */
  var conditions: String = ""
  /**
   * 重试次数
   */
  var retryCount: Int = 0

  public override fun clone(): SubscribeInfo {
    return super.clone() as SubscribeInfo
  }

  override fun toString(): String = this.toJsonString()
}