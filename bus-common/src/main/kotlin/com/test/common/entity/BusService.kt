package com.test.common.entity

import cn.sh.ideal.nj.share.common.json.toJsonString
import com.test.common.enum.UriType
import lombok.Data
import javax.persistence.*


/**
 * @author 费世程
 * @date 2020/6/4 15:36
 */
@Data
@Entity
@Table(
    name = "bus_service",
    indexes = [
      Index(name = "service_id", columnList = "service_id")
    ])
open class BusService {

  /**
   * 服务id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "service_id", unique = true, nullable = false, updatable = false,
      columnDefinition = "int(11) comment '服务id'")
  var serviceId: Int? = null
  /**
   * 服务名
   */
  @Column(name = "service_name", unique = true, nullable = false,
      columnDefinition = "varchar(64) comment '服务名'")
  var serviceName: String? = null
  /**
   * 服务描述
   */
  @Column(name = "service_desc",
      columnDefinition = "varchar(255) comment '服务描述'")
  var serviceDesc: String? = null
  /**
   * 推送地址
   */
  @Column(name = "push_uri", nullable = false,
      columnDefinition = "varchar(255) comment '推送地址'")
  var pushUri: String? = null
  /**
   * 推送地址类型
   */
  @Column(name = "uri_type", nullable = false,
      columnDefinition = "varchar(32) comment '推送地址类型'")
  var uriType: UriType? = null

  override fun toString(): String = this.toJsonString()

}