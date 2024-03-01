
package com.qhrtech.emr.restapi.models.dto.prescribeit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Conversation Contact Data Transfer Object.
 */
public class ConversationContactDto implements Serializable {

  @NotNull
  @JsonProperty("contactId")
  @Schema(description = "The unique id of the conversation contact", example = "1")
  private int contactId;

  @NotNull
  @JsonProperty("identifier")
  @Schema(description = "The unique identifier of the conversation contact",
      example = "7dfbc81e-5e18-4d24-9750-f884869b8855")
  @Size(max = 256, message = "Maximum length allowed is 256 characters.")
  private String identifier;

  @NotNull
  @JsonProperty("service")
  @Schema(description = "The service", example = "Service")
  @Size(max = 256, message = "Maximum length allowed is 256 characters.")
  private String service;

  @JsonProperty("displayName")
  @Schema(description = "The display name", example = "EMERGENCY CONTACT")
  @Size(max = 256, message = "Maximum length allowed is 256 characters.")
  private String displayName;

  @Schema(description = "The external UUID", example = "550e8400-e29b-41d4-a716-446655440000")
  @JsonProperty("externalUuid")
  private UUID externalUuid;


  public UUID getExternalUuid() {
    return externalUuid;
  }

  public void setExternalUuid(UUID externalUuid) {
    this.externalUuid = externalUuid;
  }

  /**
   * The unique id
   *
   * @return The id
   * @documentationExample 1
   */
  public int getContactId() {
    return contactId;
  }

  public void setContactId(int contactId) {
    this.contactId = contactId;
  }

  /**
   * The unique identifier
   *
   * @return The identifier
   * @documentationExample 7dfbc81e-5e18-4d24-9750-f884869b8855
   */
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * The service
   *
   * @return The service
   * @documentationExample Service
   */
  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  /**
   * The display name
   *
   * @return The display name
   * @documentationExample EMERGENCY CONTACT
   */
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConversationContactDto that = (ConversationContactDto) o;
    return contactId == that.contactId
        && Objects.equals(identifier, that.identifier)
        && Objects.equals(service, that.service)
        && Objects.equals(externalUuid, that.externalUuid)
        && Objects.equals(displayName, that.displayName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contactId, identifier, service, displayName, externalUuid);
  }

  @Override
  public String toString() {
    return "ConversationContactDto{" + "contactId=" + contactId
        + ", identifier='" + identifier + '\''
        + ", service='" + service + '\''
        + ", externalUuid='" + externalUuid + '\''
        + ", displayName='" + displayName + '\''
        + '}';
  }
}
