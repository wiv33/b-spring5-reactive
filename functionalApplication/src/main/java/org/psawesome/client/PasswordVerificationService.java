package org.psawesome.client;

import reactor.core.publisher.Mono;

/**
 * package: org.psawesome.client
 * author: PS
 * DATE: 2020-07-31 금요일 12:22
 */
public interface PasswordVerificationService {
  Mono<Void> check(String raw, String encoded);
}
