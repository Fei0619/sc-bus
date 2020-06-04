package com.test.manager

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @author 费世程
 * @date 2020/6/4 14:12
 */
@EnableJpaRepositories
@EntityScan("com.test.common.entity")
@EnableScheduling
@EnableDiscoveryClient
@SpringBootConfiguration
@SpringBootApplication(scanBasePackages = ["com.test.manager"])
class ManagerApplication

fun main(args: Array<String>) {
  System.setProperty("hibernate.dialect.storage_engine", "innodb")
  runApplication<ManagerApplication>(*args)
}