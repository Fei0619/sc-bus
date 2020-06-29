package com.test.common.pojo

import cn.sh.ideal.nj.share.common.json.toJsonString
import com.test.common.entity.BusService
import com.test.common.json.JsonUtils
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

/**
 * @author 费世程
 * @date 2020/6/8 22:18
 */
class SubscribeDetails : BusService() {

  private val logger = LoggerFactory.getLogger(SubscribeDetails::class.java)

  var subscribeId: Int = -1
  var eventCode: String? = null
  /**
   * eg: tenantId^1&userId^1,2,3|tenantId^2|tenantId^3
   */
  var conditions: String = ""
  var isBroadcast: Int = 0
  var subscribeDesc: String? = null

  /**
   * tenantId^1&userId^1,2,3|tenantId^2|tenantId^3
   */
  fun getConditionGroup(): List<Map<String, Set<String>>> {
    if (conditions.isBlank()) {
      return emptyList()
    }
    val conditionList = ArrayList<Map<String, Set<String>>>()
    val array = conditions.split("|")
    array.forEach { item ->
      val list = item.split("&")
      val map = HashMap<String, Set<String>>()
      list.forEach {
        run {
          val split = it.split(Pattern.compile("\\^"), 2)
          if (split.size == 2) {
            map.putIfAbsent(split[0], split[1].split(",").toHashSet())
          } else {
            logger.debug("消息推送条件 -> $split 格式不符合规范")
          }
        }
      }
      conditionList.add(map)
    }
    return conditionList
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