package com.test.common.pojo

import cn.sh.ideal.nj.share.common.json.toJsonString
import java.util.*

/**
 * @author 费世程
 * @date 2020/6/5 10:58
 */
class InnerEventMessage {

  var eventId: String = UUID.randomUUID().toString()
  var eventCode: String? = null
  var payload: String? = null
  var idempotentKey: String = ""
  var delayMillis: Long? = null
  var tags: Set<String> = emptySet()
  var eventTime = System.currentTimeMillis()

  override fun toString(): String = this.toJsonString()

}