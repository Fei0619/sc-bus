package com.test.dao.repository

import com.test.common.entity.BusSubscribe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author 费世程
 * @date 2020/6/9 10:33
 */
@Repository
interface SubscribeRepository : JpaRepository<BusSubscribe, Int> {
}