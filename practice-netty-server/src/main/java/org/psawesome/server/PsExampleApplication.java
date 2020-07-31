package org.psawesome.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * package: org.psawesome.server
 * author: PS
 * DATE: 2020-07-31 금요일 12:56
 */
public class PsExampleApplication {

  public static void main(String... args) {
    final HttpHandler httpHandler = RouterFunctions.toHttpHandler(
            routes(new BCryptPasswordEncoder(18))
    );

    final ReactorHttpHandlerAdapter handlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);

    HttpServer.create(8080)
            .newHandler(handlerAdapter)
            .flatMap(NettyContext::onClose)
            .block();

  }

  private static RouterFunction<ServerResponse> routes(BCryptPasswordEncoder passwordEncoder) {
    return route(POST("/check"), request -> request
            .bodyToMono(PasswordDTO.class)
            .map(dto -> passwordEncoder.matches(dto.getRaw(), dto.getEncoded()))
            .flatMap(isMatcher -> isMatcher
                    ? ServerResponse
                    .ok()
                    .build()
                    : ServerResponse
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .build()
            ));
  }

  private static class PasswordDTO {
    private String raw;
    private String encoded;

    public String getRaw() {
      return raw;
    }

    public PasswordDTO setRaw(String raw) {
      this.raw = raw;
      return this;
    }

    public String getEncoded() {
      return encoded;
    }

    public PasswordDTO setEncoded(String encoded) {
      this.encoded = encoded;
      return this;
    }
  }
}
