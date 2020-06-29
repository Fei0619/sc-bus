package com.test.common.pojo

import com.test.common.json.JsonUtils
import org.hibernate.mapping.Collection
import kotlin.collections.ArrayList

/**
 * @author 费世程
 * @date 2020/6/12 13:57
 */
class EventGenerator {

  val messages = ArrayList<InnerEventMessage<*>>()

  companion object {
    private var sign = -1

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

  fun addTag(key: String, vararg values: String): EventGenerator {
    val message = messages[sign]
    message.tags[key] = values.toHashSet()
    return this
  }

  fun addTag(key: String, values: Collection): EventGenerator {
    val message = messages[sign]
    return this
  }

}

fun main() {
  val generator = EventGenerator.create()
  generator.add("eventCode1", "payload1")
      .idempotentKey("idempotentKey1")
      .delayMillis(10)
      .addTag("key1", "value1")
      .addTag("key2", "value1", "value2")

  generator.add("eventCode2", "payload2")
      .idempotentKey("idempotentKey2")
      .delayMillis(10)
  System.err.println(JsonUtils.toJsonString(generator.messages))
}
