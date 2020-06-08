package com.test.core.repository

import com.test.common.entity.BusService
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author 费世程
 * @date 2020/6/8 17:54
 */
@Repository
interface ServiceRepository :JpaRepository<BusService,Int> {



}