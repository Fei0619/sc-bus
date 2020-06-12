package com.test.core.pojo

import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit


/**
 * @author 费世程
 * @date 2020/6/10 14:28
 */
class DelayMessage<T>(
    /**
     * 消息类型
     */
    var messageType: MessageType,
    /**
     * 到期时间
     */
    var expireTime: Long,
    /**
     * 消息实体
     */
    var message: T

) : Delayed {

  override fun compareTo(other: Delayed?): Int {
    return (this.getDelay(TimeUnit.MILLISECONDS) - other!!.getDelay(TimeUnit.MILLISECONDS)).toInt()
  }

  override fun getDelay(unit: TimeUnit): Long {
    return unit.convert(expireTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
  }

  enum class MessageType {
    DELAY_MESSAGE,
    PUSH_FAIL_MESSAGE,
    ;
  }
}