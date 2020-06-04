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
    name = "bus_config",
    indexes = [
      Index(name = "config_id", columnList = "config_id"),
      Index(name = "subscribe_id", columnList = "subscribe_id")
    ])
class BusConfig {
  /**
   * 配置主键
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "config_id", unique = true, updatable = false, nullable = false,
      columnDefinition = "int(11) comment '配置主键'")
  var configId: Int? = null
  /**
   * 订阅id
   */
  @Column(name = "subscribe_id", nullable = false,
      columnDefinition = "int(11) comment '订阅id'")
  var subscribeId: Int? = null
  /**
   * 属性
   */
  @Column(name = "property", nullable = false,
      columnDefinition = "varchar(32) comment '属性'")
  var property: String? = null
  /**
   * 属性值
   */
  @Column(name = "property_value", nullable = false,
      columnDefinition = "varchar(32) comment '属性值'")
  var propertyValue: String? = null
  /**
   * 属性描述
   */
  @Column(name = "property_desc",
      columnDefinition = "varchar(255) comment '属性描述'")
  var propertyDesc: String? = null

  override fun toString(): String = this.toJsonString()
}