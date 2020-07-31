package org.psawesome.server;

import io.netty.resolver.InetSocketAddressResolver;
import org.psawesome.common.PasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.http.server.HttpServer;
import reactor.ipc.netty.tcp.TcpServer;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * package: org.psawesome
 * author: PS
 * DATE: 2020-07-31 금요일 11:32
 */
public class StandaloneApplication {
  public static void main(String... args) {
    final HttpHandler httpHandler = RouterFunctions.toHttpHandler(
            routes(new BCryptPasswordEncoder(18))
    );

    ReactorHttpHandlerAdapter reactorHttpHandlerAdapter =
            new ReactorHttpHandlerAdapter(httpHandler);

//    HttpServer.create("", 8080)
    HttpServer.create(8080)
            .newHandler(reactorHttpHandlerAdapter)
            .flatMap(NettyContext::onClose)
            .block()
    ;

  }


  private static RouterFunction<ServerResponse> routes(
          BCryptPasswordEncoder passwordEncoder
  ) {
    return route(POST("/check"), request -> request
            .bodyToMono(PasswordDTO.class)
            .map(p -> passwordEncoder
                    .matches(p.getRaw(), p.getSecured()))
            .flatMap(isMatched -> isMatched
                    ? ServerResponse
                    .ok()
                    .build()
                    : ServerResponse
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .build())
    );
  }

}
