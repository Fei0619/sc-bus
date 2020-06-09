package com.test.dao

import com.test.common.pojo.EventSubscription
import com.test.dao.repository.EventRepository
import org.springframework.stereotype.Repository

/**
 * bus_event 为主表的查询
 *
 * @author 费世程
 * @date 2020/6/9 10:44
 */
@Repository
interface BusEventDao : EventRepository {


  /**
   * 获取所有的事件订阅信息
   * @return List<EventSubscription>
   */
  fun listAllEventSubscription(): List<EventSubscription>

}