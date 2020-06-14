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
  var tags = ArrayList<HashMap<String, Set<String>>>()
  var eventTime = System.currentTimeMillis()

  fun needDelay(): Boolean {
    return delayMillis > 0
  }

  fun matches(conditions: Set<String>?): Boolean {
    if (conditions == null || conditions.isEmpty()) return true

//    if (tags.isEmpty()) return false
//    val pattern = Pattern.compile(":")
//    val tagMap = tags
//        .map { it.split(pattern, 2) }
//        .filter { it.size == 2 }
//        .associateBy { it[0] }
//    val keys = tagMap.keys
//    conditions.forEach {
//      val array = it.split(pattern, 2)
//      if (array.size == 2) {
//        if (keys.contains(array[0]) && array[1] != tagMap.getValue(array[0])[1]) {
//          return false
//        }
//      }
//    }
    return true
  }

  override fun toString(): String = this.toJsonString()

}