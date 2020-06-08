package com.test.core.pojo

import com.test.common.json.JsonUtils
import com.test.sdk.Res
import org.springframework.http.HttpStatus

/**
 * @author 费世程
 * @date 2020/6/8 16:05
 */
class ExecutePushResponse(private val httpStatus: Int,
                          private val responseBody: String) {

  var pushTime: Long = 0L
  var responseTime: Long = 0L
  var res: Res? = null

  fun isSuccess(): Boolean {
    return if (httpStatus != HttpStatus.OK.value()) {
      false
    } else {
      res = JsonUtils.parseJson(responseBody, Res::class.java)
      true
    }
  }

}