package org.psawesome.client;

import org.psawesome.common.PsPasswordDTO;
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
 * @since 20. 8. 2. Sunday
 */
public class DefaultPasswordVerificationOverService implements PasswordVerificationOverService {
  final WebClient client;

  public DefaultPasswordVerificationOverService(WebClient.Builder builder) {
    this.client = builder
            .baseUrl("http://localhost:8080")
            .build();
  }

  @Override
  public Mono<Void> passwordCheck(String raw, String encode) {
    return this.client
            .post()
            .uri("/check")
            .body(BodyInserters.fromPublisher(
                    Mono.just(new PsPasswordDTO(raw, encode)
                    ), PsPasswordDTO.class
            ))
            .exchange()
            .flatMap(response -> {
              if (response.statusCode().is2xxSuccessful()) {
                return Mono.empty();
              } else if (response.statusCode().is4xxClientError()) {
                return Mono.error(new BadCredentialsException("Invalid!!!"));
              }
              return Mono.error(new IllegalStateException("illegal"));
            });
  }
}
