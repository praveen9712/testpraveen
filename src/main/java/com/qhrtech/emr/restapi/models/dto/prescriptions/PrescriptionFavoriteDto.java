
package com.qhrtech.emr.restapi.models.dto.prescriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * The prescription favorite transfer model.
 */
public class PrescriptionFavoriteDto {

  @JsonProperty("id")
  private int id;

  @JsonProperty("prescriptionIds")
  private List<Integer> prescriptionIds;

  @JsonProperty("name")
  private String name;

  @JsonProperty("userId")
  private Integer userId;

  /**
   * The unique id of prescription favorite.
   *
   * @documentationExample 1
   *
   * @return The unique id.
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The list of prescription ids related to this favorite.
   *
   * @return The list of prescription ids.
   */
  public List<Integer> getPrescriptionIds() {
    return prescriptionIds;
  }

  public void setPrescriptionIds(List<Integer> prescriptionIds) {
    this.prescriptionIds = prescriptionIds;
  }

  /**
   * The favorite name.
   *
   * @documentationExample cold
   *
   * @return The favorite name.
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The user id of the user who creates this favorite.
   *
   * @documentationExample 1
   *
   * @return The user id.
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
    if (!(o instanceof PrescriptionFavoriteDto)) {
      return false;
    }

    PrescriptionFavoriteDto that = (PrescriptionFavoriteDto) o;

    if (getId() != that.getId()) {
      return false;
    }
    if (getPrescriptionIds() != null ? !getPrescriptionIds().equals(that.getPrescriptionIds())
        : that.getPrescriptionIds() != null) {
      return false;
    }
    if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
      return false;
    }
    return getUserId() != null ? getUserId().equals(that.getUserId()) : that.getUserId() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getPrescriptionIds() != null ? getPrescriptionIds().hashCode() : 0);
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PrescriptionFavoriteDto{");
    sb.append("id=").append(id);
    sb.append(", prescriptionIds=").append(prescriptionIds);
    sb.append(", name='").append(name).append('\'');
    sb.append(", userId=").append(userId);
    sb.append('}');
    return sb.toString();
  }
}
