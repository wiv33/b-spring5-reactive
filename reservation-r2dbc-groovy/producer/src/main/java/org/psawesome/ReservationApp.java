package org.psawesome;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.r2dbc.repository.query.R2dbcQueryMethod;

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
@SpringBootApplication
@EnableR2dbcRepositories
public class ReservationApp {

  public static void main(String[] args) {
    SpringApplication.run(ReservationApp.class, args);
  }

  @Bean
  CommandLineRunner setUp(ReservationRepository rr) {
    return args -> {
      // TODO
//      2020-09-10 23:55:22.853 ERROR 83930 --- [tor-tcp-epoll-1] reactor.Mono.DefaultIfEmpty.1            : onError(org.springframework.data.r2dbc.BadSqlGrammarException: executeMany; bad SQL grammar [INSERT INTO reservation (name) VALUES ($1)]; nested exception is io.r2dbc.postgresql.ExceptionFactory$PostgresqlBadGrammarException: [42P01] relation "reservation" does not exist)
      rr.save(new Reservation(null, "awesome"))
              .log()
              .subscribe(System.out::println);
    };
  }
}
