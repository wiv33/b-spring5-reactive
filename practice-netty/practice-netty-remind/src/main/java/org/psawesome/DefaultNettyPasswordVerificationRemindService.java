package org.psawesome;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
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
public class DefaultNettyPasswordVerificationRemindService implements PasswordVerificationRemindService {
  final WebClient client;

  public DefaultNettyPasswordVerificationRemindService(WebClient.Builder builder) {
    this.client = builder
            .baseUrl("http://localhost:8080")
            .build();
  }

  @Override
  public Mono<Void> check(String raw, String encode) {
    return this.client
            .post()
            .uri("/check")
            .body(BodyInserters.fromPublisher(
                    Mono.just(new NettyPasswordDTO(raw, encode))
                    , NettyPasswordDTO.class))
            .exchange()
            .flatMap(response -> {
              if (response.statusCode().is2xxSuccessful()) {
                return Mono.empty();
              } else if (response.statusCode().is4xxClientError()) {
                return Mono.error(new BadCredentialsException("invalid your request !"));
              }
              return Mono.error(new IllegalStateException(""));
            });
  }
}
