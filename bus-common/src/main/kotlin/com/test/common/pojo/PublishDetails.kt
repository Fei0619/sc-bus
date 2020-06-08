package com.test.common.pojo

import cn.sh.ideal.nj.share.common.json.toJsonString
import com.test.common.date.DateUtils


/**
 * @author 费世程
 * @date 2020/6/4 22:49
 */
class PublishDetails {

  var id: Int? = null
  var createTimestamp: Long = System.currentTimeMillis()
  var createTime = DateUtils.parse(createTimestamp)
  /**
   * 事件唯一id
   */
  var eventId: String = ""
  /**
   * 事件编码
   */
  var eventCode: String = ""
  /**
   * 是否延迟推送
   */
  var needDelay: Boolean = false
  /**
   * 事件详情
   */
  var eventMessage: InnerEventMessage? = null
  /**
   * 订阅者信息列表
   */
  var subscribes: List<SubscribeInfo> = emptyList()

  override fun toString(): String = this.toJsonString()

}