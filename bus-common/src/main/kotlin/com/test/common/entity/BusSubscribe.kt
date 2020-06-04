package com.test.common.entity

import cn.sh.ideal.nj.share.common.json.toJsonString
import lombok.Data
import javax.persistence.*

/**
 * @author 费世程
 * @date 2020/6/4 16:03
 */
@Data
@Entity
@Table(
    name = "bus_subscribe",
    indexes = [
      Index(name = "subscribe_id", columnList = "subscribe_id"),
      Index(name = "service_id", columnList = "service_id"),
      Index(name = "event_id", columnList = "event_id")
    ])
class BusSubscribe {

  /**
   * 订阅id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "subscribe_id", unique = true, updatable = false, nullable = false,
      columnDefinition = "int(11) comment '订阅id'")
  var subscribeId: Int? = null
  /**
   * 服务id
   */
  @Column(name = "service_id", nullable = false,
      columnDefinition = "int(11) comment '服务id'")
  var serviceId: Int? = null
  /**
   * 事件id
   */
  @Column(name = "event_id", nullable = false,
      columnDefinition = "int(11) comment '事件id'")
  var eventId: Int? = null
  /**
   * 推送条件
   */
  @Column(name = "conditions",
      columnDefinition = "varchar(255) comment '推送条件'")
  var conditions: String? = null
  /**
   * 是否广播
   */
  @Column(name = "is_broadcast", nullable = false,
      columnDefinition = "boolean comment '是否广播'")
  var isBroadcast: Boolean? = null
  /**
   * 订阅说明
   */
  @Column(name = "subscribe_desc",
      columnDefinition = "varchar(255) comment '订阅说明'")
  var subscribeDesc: String? = null

  override fun toString(): String = this.toJsonString()

}