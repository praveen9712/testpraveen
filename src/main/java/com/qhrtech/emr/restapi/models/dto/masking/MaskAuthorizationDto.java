
package com.qhrtech.emr.restapi.models.dto.masking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.validators.CheckLocalDateTimeRange;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MaskAuthorizationDto implements Serializable {

  @JsonProperty("id")
  @Schema(description = "Mask authorization id", example = "32")
  private Integer id;

  @NotNull
  @JsonProperty("maskId")
  @Schema(description = "Mask id", example = "22")
  private int maskId;

  @CheckLocalDateTimeRange
  @JsonProperty("untilDate")
  @Schema(description = "Until date", example = "2022-12-31T23:59:00.000")
  private LocalDateTime untilDate;

  @JsonProperty("Role id")
  @Schema(description = "Role ID", example = "2")
  private Integer roleId;

  @JsonProperty("userId")
  @Schema(description = "User ID", example = "6")
  private Integer userId;

  /**
   * A unique mask authorization ID
   *
   * @documentationExample 1
   * @return Authorization ID
   */
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * A unique Mask ID
   *
   * @documentationExample 12
   * @return Mask ID
   */
  public int getMaskId() {
    return maskId;
  }

  public void setMaskId(int maskId) {
    this.maskId = maskId;
  }

  /**
   * Date time until mask is accessible to User or Role.
   *
   * @documentationExample 2017-11-29T00:00:00.000
   * @return LocalDateTime
   */
  @DocumentationExample("2017-11-08T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getUntilDate() {
    return untilDate;
  }

  public void setUntilDate(LocalDateTime untilDate) {
    this.untilDate = untilDate;
  }

  /**
   * A Role ID to which mask is accessible.
   *
   * @documentationExample 12
   * @return Role ID
   */
  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  /**
   * Accuro User ID to which mask is accessible.
   *
   * @documentationExample 12
   * @return User ID
   */
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MaskAuthorizationDto that = (MaskAuthorizationDto) o;

    if (maskId != that.maskId) {
      return false;
    }
    if (!Objects.equals(id, that.id)) {
      return false;
    }
    if (!Objects.equals(untilDate, that.untilDate)) {
      return false;
    }
    if (!Objects.equals(roleId, that.roleId)) {
      return false;
    }
    return Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + maskId;
    result = 31 * result + (untilDate != null ? untilDate.hashCode() : 0);
    result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    return result;
  }
}
