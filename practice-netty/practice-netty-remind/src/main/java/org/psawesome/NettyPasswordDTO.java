package org.psawesome;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author ps [https://github.com/wiv33/b-spring5-reactive]
 * @role
 * @responsibility
 * @cooperate {
 * input:
 * output:
 * }
 * @see
 * @since 20. 9. 16. Wednesday
 */
@Getter
public class NettyPasswordDTO {
  private final String raw;
  private final String encode;

  @JsonCreator
  public NettyPasswordDTO(@JsonProperty("raw") String raw,
                          @JsonProperty("encode") String encode) {
    this.raw = raw;
    this.encode = encode;
  }
}
