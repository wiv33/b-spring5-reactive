package org.psawesome;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 8. 8. Saturday
 */
@SpringBootTest
public class TemplateEngineAppTest {

  @Autowired
  ApplicationContext context;

  @Mock
  RouterFunction<ServerResponse> routerFunction;

  @Test
  void testApplicationRun() {
    final RouterFunction<ServerResponse> spy = Mockito.spy(routerFunction);
    when(RouterFunctions.route(GET("/"), any()))
            .thenReturn(spy)
    ;
  }
}
