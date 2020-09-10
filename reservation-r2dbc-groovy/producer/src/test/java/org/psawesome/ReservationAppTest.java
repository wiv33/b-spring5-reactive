package org.psawesome;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
class ReservationAppTest {
  @Test
  void createTest() {
    var reservation = new Reservation(1, "ps");

    assertEquals(((Integer) 1), reservation.getId());
    assertEquals("ps", reservation.getName());
  }
}
