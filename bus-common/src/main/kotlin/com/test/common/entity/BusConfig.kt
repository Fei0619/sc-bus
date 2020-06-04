package com.test.common.entity

import lombok.Data
import javax.persistence.Entity
import javax.persistence.Table

/**
 * @author 费世程
 * @date 2020/6/4 16:09
 */
@Data
@Entity
@Table(
    name = "bus-config",
    indexes = [
    ])
class BusConfig {
}