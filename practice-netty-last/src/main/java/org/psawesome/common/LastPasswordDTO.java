package org.psawesome.common;

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
  private final String encode;

  public LastPasswordDTO(String raw, String encode) {
    this.raw = raw;
    this.encode = encode;
  }

  public String getRaw() {
    return raw;
  }

  public String getEncode() {
    return encode;
  }
}
