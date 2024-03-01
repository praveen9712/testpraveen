
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import javax.ws.rs.core.Response;

@Schema(description = "Referral status of the generated letter")
public enum ReferralStatus {

  /**
   * Status as file not available
   */
  NO_FILE(0),

  /**
   * Status as file is available
   */
  FILE_READY(1),

  /**
   * Status as file is retrieved.
   */
  FILE_RETRIEVED(2);

  private final int statusId;

  ReferralStatus(int statusId) {
    this.statusId = statusId;
  }

  public int getStatusId() {
    return this.statusId;
  }

  public static ReferralStatus lookup(int statusId) {
    ReferralStatus[] statuses = values();

    for (ReferralStatus status : statuses) {
      if (status.getStatusId() == statusId) {
        return status;
      }
    }

    return null;
  }

  @JsonCreator
  public static ReferralStatus fromString(String status) {
    return Arrays.stream(values()).filter(e -> e.name().equals(status)).findFirst()
        .orElseThrow(() -> Error.webApplicationException(Response.Status.BAD_REQUEST,
            "Invalid generated letter status: " + status));
  }
}
