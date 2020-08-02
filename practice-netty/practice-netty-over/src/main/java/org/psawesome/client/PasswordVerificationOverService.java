package org.psawesome.client;

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
 * @since 20. 8. 2. Sunday
 */
public interface PasswordVerificationOverService {
  Mono<Void> passwordCheck(String raw, String encode);
}
