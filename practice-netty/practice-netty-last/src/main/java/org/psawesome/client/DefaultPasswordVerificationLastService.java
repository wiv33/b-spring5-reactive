package org.psawesome.client;

import org.psawesome.common.LastPasswordDTO;
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
public class DefaultPasswordVerificationLastService implements PasswordVerificationLastService {
  final WebClient client;

  public DefaultPasswordVerificationLastService(WebClient.Builder builder) {
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
//                    DB 조회한 값을 서버에 보내서 확인하는 곳
                    Mono.just(new LastPasswordDTO(raw, encode)),
                    LastPasswordDTO.class
            ))
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
