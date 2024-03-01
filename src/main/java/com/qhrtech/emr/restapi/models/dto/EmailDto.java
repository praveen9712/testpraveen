
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * The Email data transfer object model.
 *
 * @author jesse.pasos
 * @see com.qhrtech.emr.accuro.model.demographics.Email
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Email data transfer object model. If emailId and type field is null,"
    + "  it will not be shown in the response.",
    implementation = EmailDto.class, name = "EmailDto")
public class EmailDto {

  @JsonProperty("emailId")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The id of the email", type = "integer", example = "1")
  private Integer emailId;

  @JsonProperty("type")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The type of the email", type = "string", example = "Business")
  private String type;

  @JsonProperty("address")
  @Schema(description = "The email address", type = "string", format = "email",
      example = "contact@doctor.com")
  private String address;

  @JsonProperty("order")
  @Schema(description = "The order of the email", type = "integer", required = true, example = "1")
  private int order;

  /**
   * Unique email identifier
   *
   * @return Email ID
   * @documentationExample 1
   */
  public Integer getEmailId() {
    return emailId;
  }

  public void setEmailId(Integer emailId) {
    this.emailId = emailId;
  }

  /**
   * Type for the email.
   *
   * @return Email type
   * @documentationExample Business
   */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * Address for the Email
   *
   * @return An email address
   * @documentationExample contact@doctor.com
   */
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Order for the Email
   *
   * @return A numeric order value
   * @documentationExample 1
   */
  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 53 * hash + Objects.hashCode(this.emailId);
    hash = 53 * hash + Objects.hashCode(this.type);
    hash = 53 * hash + Objects.hashCode(this.address);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final EmailDto other = (EmailDto) obj;
    if (!Objects.equals(this.type, other.type)) {
      return false;
    }
    if (!Objects.equals(this.address, other.address)) {
      return false;
    }
    if (!Objects.equals(this.emailId, other.emailId)) {
      return false;
    }
    return true;
  }

}
