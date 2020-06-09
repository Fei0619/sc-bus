package com.test.core.processor

import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2020/6/9 14:08
 */
interface IdempotentProcessor {

  /**
   * 幂等判断：在【expireTimeMillis】时间内出现过【idempotentKey】则返回false
   * @param idempotentKey 幂等key
   * @param expireTimeMillis 过期时间
   */
  fun idempotent(idempotentKey: String, expireTimeMillis: Long): Mono<Boolean>

}