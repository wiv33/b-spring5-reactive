package org.psawesome.testFlux;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.LinkedList;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 7. 25. Saturday
 */
public class GroupByTest {
  public static final Logger log = LoggerFactory.getLogger(GroupByTest.class);

  @Test
  void testGroupBy() {
    Flux.range(1, 7)
            .groupBy(e -> e % 2 == 0 ? "even" : "odd")
            .subscribe(groupFlux -> groupFlux
                    .scan(new LinkedList<>(),
                            (list, ele) -> {
                              list.add(ele);
                              if (list.size() > 2) {
                                list.remove(0);
                              }
                              return list;
                            })
                    .filter(arr -> !arr.isEmpty())
                    .subscribe(data -> log.info("{}: {}", groupFlux.key(), data)));
    /*
20:16:17.956 [Test worker] INFO org.psawesome.testFlux.GroupByTest - odd: [1]
20:16:17.957 [Test worker] INFO org.psawesome.testFlux.GroupByTest - even: [2]
20:16:17.958 [Test worker] INFO org.psawesome.testFlux.GroupByTest - odd: [1, 3]
20:16:17.958 [Test worker] INFO org.psawesome.testFlux.GroupByTest - even: [2, 4]
20:16:17.958 [Test worker] INFO org.psawesome.testFlux.GroupByTest - odd: [3, 5]
20:16:17.958 [Test worker] INFO org.psawesome.testFlux.GroupByTest - even: [4, 6]
20:16:17.958 [Test worker] INFO org.psawesome.testFlux.GroupByTest - odd: [5, 7]
     */
  }
}
