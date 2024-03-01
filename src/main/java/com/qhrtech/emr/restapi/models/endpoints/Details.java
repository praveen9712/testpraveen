
package com.qhrtech.emr.restapi.models.endpoints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.OfficeDto;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;

/**
 * This entity represents details about an Accuro User authenticated via the Api.
 *
 * @author kevin.kendall
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Details about an Accuro user authenticated via API")
public class Details {

  @JsonProperty("userName")
  @Schema(description = "User name", example = "John")
  private String userName;

  @JsonProperty("officeNames")
  @Schema(description = "List of office names", example = "[\"Eastern Office\",\"Head Office\"]")
  private List<String> officeNames;

  @JsonProperty("scopes")
  @Schema(description = "List of scopes", example = "[\"LABS_WRITE\",\"LABS_READ\"]")
  private Set<String> scopes;

  @JsonProperty("grantType")
  @Schema(description = "Type of grant", example = "password")
  private String grantType;

  @JsonProperty("currentOffice")
  @Schema(description = "Details of current office")
  @Valid
  private OfficeDto currentOffice;

  /**
   * Username of the authenticated user.
   *
   * @return A username String
   *
   * @documentationExample DavidDoctor
   */
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Names of the offices the authenticated user has roles in (and thus could authenticate to).
   *
   * @return An ordered collection of office names
   */
  @DocumentationExample(value = "Downtown Office", value2 = "Test Office")
  public List<String> getOfficeNames() {
    return officeNames;
  }

  public void setOfficeNames(List<String> officeNames) {
    this.officeNames = officeNames;
  }

  /**
   * Oauth scopes the authenticated user currently has.
   *
   * @return An unordered collection of scopes
   */
  @DocumentationExample(value = "CLINICAL_NOTES_READ", value2 = "CLINICAL_NOTES_WRITE")
  public Set<String> getScopes() {
    return scopes;
  }

  public void setScopes(Set<String> scopes) {
    this.scopes = scopes;
  }

  /**
   * The Oauth Grant Type the user is authenticated under.
   *
   * @return A grant type String
   *
   * @documentationExample password
   */
  public String getGrantType() {
    return grantType;
  }

  public void setGrantType(String grantType) {
    this.grantType = grantType;
  }

  /**
   * The Office the user is authenticated to.
   *
   * @return An Office DTO
   */
  public OfficeDto getCurrentOffice() {
    return currentOffice;
  }

  public void setCurrentOffice(OfficeDto currentOffice) {
    this.currentOffice = currentOffice;
  }

}
