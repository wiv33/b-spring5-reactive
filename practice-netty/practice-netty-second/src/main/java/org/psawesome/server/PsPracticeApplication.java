package org.psawesome.server;

import org.psawesome.common.SecondPasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 7. 31. Friday
 */
public class PsPracticeApplication {
  public static void main(String... args) {
    final HttpHandler httpHandler = RouterFunctions.toHttpHandler(
            routes(new BCryptPasswordEncoder(18))
    );

    final ReactorHttpHandlerAdapter httpHandlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);

    HttpServer.create(8080)
            .newHandler(httpHandlerAdapter)
            .flatMap(NettyContext::onClose)
            .block();
  }

  private static RouterFunction<ServerResponse> routes(PasswordEncoder encoder) {
    return route(POST("/check"), request -> request
            .bodyToMono(SecondPasswordDTO.class)
            .map(sDto -> encoder.matches(sDto.getRaw(), sDto.getSecured()))
            .flatMap(isMatcher -> isMatcher
                    ? ServerResponse
                    .ok()
                    .build()
                    : ServerResponse
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .build()));
  }
}
