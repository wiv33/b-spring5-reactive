package org.psawesome.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class LastPasswordDTO {
  private final String raw;
  private final String test;

  @JsonCreator
  public LastPasswordDTO(@JsonProperty("raw") String raw,
                         @JsonProperty("test") String test) {
    this.raw = raw;
    this.test = test;
  }

  public String getRaw() {
    return raw;
  }

  public String getTest() {
    return test;
  }
}
