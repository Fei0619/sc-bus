package com.test.common.entity

import cn.sh.ideal.nj.share.common.json.toJsonString
import lombok.Data
import javax.persistence.*

/**
 * @author 费世程
 * @date 2020/6/4 16:09
 */
@Data
@Entity
@Table(
    name = "bus-event",
    indexes = [
      Index(name = "event_id", columnList = "event_id"),
      Index(name = "event_code", columnList = "event_code")
    ])
class BusEvent {
  /**
   * 事件id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "event_id", unique = true, updatable = false, nullable = false,
      columnDefinition = "int(11) comment '事件id'")
  var eventId: Int? = null
  /**
   * 事件编码
   */
  @Column(name = "event_code", unique = true, nullable = false,
      columnDefinition = "varchar(64) comment '事件编码'")
  var eventCode: String? = null
  /**
   * 事件名称
   */
  @Column(name = "event_name",
      columnDefinition = "varchar(64) comment '事件名称'")
  var eventName: String? = null
  /**
   * 事件说明
   */
  @Column(name = "event_desc",
      columnDefinition = "varchar(255) comment '事件说明'")
  var eventDesc: String? = null

  override fun toString(): String = this.toJsonString()

}