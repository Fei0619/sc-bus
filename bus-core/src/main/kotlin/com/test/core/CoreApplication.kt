package com.test.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @author 费世程
 * @date 2020/6/4 23:20
 */
@EnableJpaRepositories
@EntityScan("com.test.common.entity")
@EnableScheduling
@EnableDiscoveryClient
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = ["com.test.core", "com.test.dao"])
class CoreApplication

fun main(args: Array<String>) {
  System.setProperty("hibernate.dialect.storage_engine", "innodb")
  runApplication<CoreApplication>(*args)
}