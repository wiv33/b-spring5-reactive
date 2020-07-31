package org.psawesome.client;

import org.psawesome.common.PasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * package: org.psawesome.client
 * author: PS
 * DATE: 2020-07-31 금요일 12:23
 */
public class DefaultPasswordVerificationService implements PasswordVerificationService {
  final WebClient client;

  public DefaultPasswordVerificationService(WebClient.Builder builder) {
    this.client = builder
            .baseUrl("http://localhost:8080")
            .build();
  }

  @Override
  public Mono<Void> check(String raw, String encoded) {
    return client
            .post()
            .uri("/check")
            .body(BodyInserters.fromPublisher(
                    Mono.just(new PasswordDTO(raw, encoded)),
                    PasswordDTO.class
            )).exchange()
            .flatMap(response -> {
              if (response.statusCode().is2xxSuccessful()) {
                return Mono.empty();
              } else if (response.statusCode() == HttpStatus.EXPECTATION_FAILED) {
                return Mono.error(new BadCredentialsException(""));
              }
              return Mono.error(new IllegalStateException());
            })
            ;
  }
}
