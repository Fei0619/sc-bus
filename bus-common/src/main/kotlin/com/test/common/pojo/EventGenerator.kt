package com.test.common.pojo

import kotlin.collections.ArrayList

/**
 * @author 费世程
 * @date 2020/6/12 13:57
 */
class EventGenerator {

  val messages = ArrayList<InnerEventMessage<*>>()

  companion object {
    private var sign = -1
    private var filterSign = -1

    fun create(): EventGenerator {
      return EventGenerator()
    }

    fun create(eventCode: String, payload: Any): EventGenerator {
      val generator = EventGenerator()
      val message = InnerEventMessage<Any>()
      message.eventCode = eventCode
      message.payload = payload
      generator.messages.add(message)
      sign++
      return generator
    }

  }

  fun add(eventCode: String, payload: Any): EventGenerator {
    val message = InnerEventMessage<Any>()
    message.eventCode = eventCode
    message.payload = payload
    messages.add(message)
    sign++
    return this
  }

  fun idempotentKey(idempotentKey: String): EventGenerator {
    val message = messages[sign]
    message.idempotentKey = idempotentKey
    return this
  }

  fun delayMillis(delayMillis: Long): EventGenerator {
    val message = messages[sign]
    message.delayMillis = delayMillis
    return this
  }

  fun and(key: String, vararg values: String): EventGenerator {
    val message = messages[sign]
    val map = message.tags[filterSign]
    map[key] = values.toHashSet()
    return this
  }

  fun and(key: String, values: Collection<String>): EventGenerator {
    val message = messages[sign]
    val map = message.tags[filterSign]
    map[key] = values.toHashSet()
    return this
  }

  fun or(key: String, vararg values: String): EventGenerator {
    val message = messages[sign]
    val map = HashMap<String, Set<String>>()
    map[key] = values.toHashSet()
    message.tags.add(map)
    return this
  }

  fun or(key: String, values: Collection<String>): EventGenerator {
    val message = messages[sign]
    val map = HashMap<String, Set<String>>()
    map[key] = values.toHashSet()
    message.tags.add(map)
    return this
  }

}

fun main() {
  val generator = EventGenerator.create()
//  generator.add("topic1", "")
//      .delayMillis(60)
//      .idempotentKey("idempotentKey1").and("key1", "value1")
//  generator.add("topic2", "")
//      .delayMillis(100)
//      .idempotentKey("idempotentKey2").and("key2", "value1", "value2")
//  System.err.println(JsonUtils.toJsonString(generator.messages))
}
