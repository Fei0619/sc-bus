package com.test.common.pojo

import cn.sh.ideal.nj.share.common.json.toJsonString
import org.hibernate.validator.constraints.NotEmpty
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @author 费世程
 * @date 2020/6/5 10:58
 */
class InnerEventMessage<T> {

  var eventId: String = UUID.randomUUID().toString()
  @NotEmpty(message = "事件主题不能为空！")
  var eventCode: String = ""
  @NotEmpty(message = "推送内容不能为空！")
  var payload: T? = null
  var idempotentKey: String = ""
  var delayMillis: Long = 0
  var tags = HashMap<String, Set<String>>()
  var eventTime = System.currentTimeMillis()

  fun needDelay(): Boolean {
    return delayMillis > 0
  }

  fun matches(conditions: List<Map<String, Set<String>>>?): Boolean {
    // conditions -> 订阅的条件
    // tags       -> 推送条件
    var i = 0
    if (conditions == null || conditions.isEmpty()) return true
    val pubKeys = tags.keys
    if (pubKeys.isEmpty()) return false
    for (map in conditions) {
      val subKeys = map.keys
      if (subKeys.isEmpty()) {
        i++
        continue
      }
      for (subKey in subKeys) {
        if (!pubKeys.contains(subKey)) {
          i++
          continue
        }
        val pub = tags[subKey]
        val sub = map[subKey]
        if (!pub!!.containsAll(sub!!)) {
          i++
          continue
        } else {
          break
        }
      }
    }
    if (i > conditions.size) {
      return false
    }
    return true
  }

  override fun toString(): String = this.toJsonString()

}