package org.psawesome.server;

import org.psawesome.common.LastPasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

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
public class LastDemoApplication {
  public static void main(String... args) {

    final HttpHandler httpHandler = RouterFunctions.toHttpHandler(
            RouterFunctions.route(POST("/check"), request -> request
                    .bodyToMono(LastPasswordDTO.class)
                    .map(dto -> {
                      final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(18);
                      return encoder.matches(dto.getRaw(), dto.getEncode());
                    })
                    .flatMap(isMatch -> isMatch
                            ? ServerResponse
                            .ok()
                            .build()
                            : ServerResponse
                            .status(HttpStatus.EXPECTATION_FAILED)
                            .build())
            )
    );

    final ReactorHttpHandlerAdapter handlerAdapter =
            new ReactorHttpHandlerAdapter(httpHandler);

    HttpServer.create(8080)
            .newHandler(handlerAdapter)
            .flatMap(NettyContext::onClose)
            .block();
  }
}
