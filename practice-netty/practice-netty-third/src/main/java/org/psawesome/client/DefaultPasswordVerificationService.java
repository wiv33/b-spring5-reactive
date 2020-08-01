package org.psawesome.client;

import org.psawesome.common.ThirdPasswordDTO;
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
 * @since 20. 8. 1. Saturday
 */
public class DefaultPasswordVerificationService implements PasswordVerificationService {
  final WebClient client;

  public DefaultPasswordVerificationService(WebClient.Builder builder) {
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
                    Mono.just(new ThirdPasswordDTO(raw, encode))
            , ThirdPasswordDTO.class))
            .exchange()
            .flatMap(response -> {
              if (response.statusCode().is2xxSuccessful()) {
                return Mono.empty();
              } else if (response.statusCode().is4xxClientError()) {
                return Mono.error(new BadCredentialsException("invalid"));
              }
              return Mono.error(new IllegalStateException());
            });
  }
}
