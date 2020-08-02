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
 * @since 20. 8. 2. Sunday
 */
public class PsPasswordDTO {
  private final String raw;
  private final String secured;

  @JsonCreator
  public PsPasswordDTO(@JsonProperty("raw") String raw,
                       @JsonProperty("secured") String secured) {
    this.raw = raw;
    this.secured = secured;
  }

  public String getRaw() {
    return raw;
  }

  public String getSecured() {
    return secured;
  }
}
