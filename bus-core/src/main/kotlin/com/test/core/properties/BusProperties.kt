package com.test.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.stereotype.Component

/**
 * @author 费世程
 * @date 2020/6/4 23:35
 */
@Component
@RefreshScope
@SuppressWarnings("unused")
@ConfigurationProperties(prefix = "sc.bus")
class BusProperties {

  var modules: List<Module> = ArrayList()
  /**
   * 指定以何种方式保存需要推送的事件
   */
  var publishType: PublishType = PublishType.Auto
  /**
   * 指定幂等key的保存方式
   */
  var idempotentStorage: IdempotentStorage = IdempotentStorage.Local
  /**
   * 推送日志的保存方式
   */
  var logStorage: LogStorage = LogStorage.Console
  /**
   * 指定以何种方式对产生的日志进行中转
   */
  var logTransferType: LogTransferType = LogTransferType.None
  /**
   * 幂等过期时间
   */
  var idempotentExpireSecond: Long = 3600L

  //------------------------------------------ 枚举 -----------------------------------------------//

  enum class PublishType {
    /**
     * 根据module情况自动选择
     */
    Auto,
    /**
     * 同步发布消息，会一直阻塞到订阅方完成响应
     */
    Sync,
    /**
     * 将发布的消息放入内存队列中，程序正常退出时会写入文件中,异常退出可能会丢失消息
     */
    Async,
    /**
     * 将发布的消息放入kafka,然后由消费者推送消息
     */
    Kafka,
    /**
     * 将发布的事件放入rabbitmq,然后由消费者推送消息
     */
    Rabbit,
    ;
  }

  enum class LogTransferType {
    /**
     * 无日志中转,直接调用日志存储器
     */
    None,
    /**
     * 将发布的消息放入kafka,然后由消费者推送消息
     */
    Kafka,
    ;
  }

  enum class LogStorage {
    /**
     * 仅将日志打印在控制台中
     */
    Console,
    /**
     * 日志保存在mongoDB中
     */
    Mongo,
    ;
  }

  enum class IdempotentStorage {
    /**
     * 保存在本机中
     */
    Local,
    /**
     * 保存在redis中
     */
    Redis,
    ;
  }

}