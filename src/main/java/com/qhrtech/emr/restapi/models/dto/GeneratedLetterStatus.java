
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import java.util.Arrays;
import javax.ws.rs.core.Response;

public enum GeneratedLetterStatus {
  /**
   * Status as sent
   */
  SENT("Sent"),

  /**
   * Status as waiting
   */
  WAITING("Waiting"),

  /**
   * Status as failed
   */
  FAILED("Failed"),

  /**
   * Status as queued
   */
  QUEUED("Queued"),

  /**
   * Status as printed
   */
  PRINTED("Printed");

  private final String status;

  GeneratedLetterStatus(String status) {
    this.status = status;
  }

  public static GeneratedLetterStatus lookup(String status) {
    return Arrays.stream(values()).filter(e -> e.status.equals(status)).findFirst().orElse(null);
  }

  @JsonCreator
  public static GeneratedLetterStatus fromString(String status) {
    return Arrays.stream(values()).filter(e -> e.name().equals(status)).findFirst()
        .orElseThrow(() -> {
          return Error.webApplicationException(Response.Status.BAD_REQUEST,
              "Invalid generated letter status: " + status);
        });
  }
}
