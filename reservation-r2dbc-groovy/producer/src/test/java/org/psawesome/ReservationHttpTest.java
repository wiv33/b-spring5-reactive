package org.psawesome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static reactor.core.publisher.Mono.when;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 9. 12. Saturday
 */
@WebFluxTest
@Import(ReservationHttpConfiguration.class)
@ExtendWith(SpringExtension.class)
public class ReservationHttpTest {

  @MockBean
  ReservationRepository
          repository;

  @Autowired
  WebTestClient testClient;

  @Test
  void testGet() {
    assertNotNull(repository);
    when(repository.findAll())
            .thenReturn(Flux.just(new Reservation(1, "ps")));

    this.testClient
            .get()
            .uri("/reservation")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("@.[0].name")
            .isEqualTo("ps");

    Mockito.verify(repository).findAll();
    Mockito.verifyNoMoreInteractions(repository);

  }
}
