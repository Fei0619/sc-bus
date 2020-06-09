package com.test.dao.repository

import com.test.common.entity.BusEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author 费世程
 * @date 2020/6/9 10:34
 */
@Repository
interface EventRepository : JpaRepository<BusEvent, Int> {
}