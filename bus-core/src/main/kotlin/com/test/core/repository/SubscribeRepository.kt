package com.test.core.repository

import com.test.common.entity.BusSubscribe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author 费世程
 * @date 2020/6/8 18:00
 */
@Repository
interface SubscribeRepository : JpaRepository<BusSubscribe, Int> {
}