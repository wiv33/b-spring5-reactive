package org.psawesome.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * package: org.psawesome.common
 * author: PS
 * DATE: 2020-07-31 금요일 11:52
 */
public class PasswordDTO {
  private String raw;
  private String secured;


  @JsonCreator
  public PasswordDTO(@JsonProperty("raw") String raw,
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

  public void setRaw(String raw) {
    this.raw = raw;
  }

  public void setSecured(String secured) {
    this.secured = secured;
  }
}
