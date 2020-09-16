package org.psawesome;

import reactor.core.publisher.Mono;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 9. 16. Wednesday
 */
public interface PasswordVerificationRemindService {
  Mono<Void> check(String raw, String encode);
}
