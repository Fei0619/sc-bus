package com.test.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @author 费世程
 * @date 2020/6/4 23:20
 */
@EnableScheduling
@EnableDiscoveryClient
@EnableConfigurationProperties
@SpringBootApplication
class CoreApplication

fun main(args: Array<String>) {
  System.setProperty("hibernate.dialect.storage_engine", "innodb")
  runApplication<CoreApplication>(*args)

}