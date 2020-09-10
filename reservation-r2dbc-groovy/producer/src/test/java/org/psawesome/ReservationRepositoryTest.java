package org.psawesome;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 9. 10. Thursday
 */
@ExtendWith(SpringExtension.class)
@DataR2dbcTest
class ReservationRepositoryTest {

  @Autowired
  ReservationRepository repository;

  @Test
  void persistence() {
    final Flux<Reservation> ps = this.repository.deleteAll()
            .thenMany(this.repository.save(new Reservation(null, "ps")))
            .thenMany(this.repository.findAll());

    StepVerifier.create(ps)
            .expectNextMatches(pred -> pred.getId().equals(1) && pred.getName().equalsIgnoreCase("ps"))
            .verifyComplete();
  }
}
