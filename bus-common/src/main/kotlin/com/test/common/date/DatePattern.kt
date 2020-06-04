package com.test.common.date

/**
 * @author 宋志宗
 * @date 2019/9/18
 */
object DatePattern {
  /** 2020-12-12 */
  const val yyyy_MM_dd = "yyyy-MM-dd"
  /** 2020-12-12 19 */
  const val yyyy_MM_dd_HH = "yyyy-MM-dd HH"
  /** 2020-12-12 19:21 */
  const val yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm"
  /** 2020-12-12 19:21:56 */
  const val yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss"
  /** 2020-12-12 19:21:56.555 */
  const val yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS"
  /** 12-12 19:21:56 */
  const val MM_dd_HH_mm_ss = "MM-dd HH:mm:ss"
  /** 12-12 19 */
  const val MM_dd_HH = "MM-dd HH"
  /** 19:21:56 */
  const val HH_mm_ss = "HH:mm:ss"
  /** 19:21 */
  const val HH_mm = "HH:mm"
}