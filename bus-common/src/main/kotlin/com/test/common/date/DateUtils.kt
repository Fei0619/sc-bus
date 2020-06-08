package com.test.common.date

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author 费世程
 * @date 2020/6/5 11:20
 */
object DateUtils {

  /** +8时区 */
  val CHINA_ZONE_OFFSET: ZoneOffset = ZoneOffset.of("+8")

  /**
   * [timestamp]转换为[LocalDateTime],默认[zoneOffset]为+8
   * @param timeStamp
   * @param zoneOffset ZoneOffset
   * @return LocalDateTime
   */
  @JvmOverloads
  fun parse(timeStamp: Long, zoneOffset: ZoneOffset = this.CHINA_ZONE_OFFSET): LocalDateTime {
    val instant = Instant.ofEpochMilli(timeStamp)
    return LocalDateTime.ofInstant(instant, zoneOffset)
  }

}