
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * <p>
 * Model object for a schedules availability template
 * </p>
 * <p>
 * This entity represents a template for applying availabilities to the scheduler.
 * </p>
 *
 * @author jesse.pasos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailabilityTemplateDto {

  @JsonProperty("templateId")
  private int templateId;

  @JsonProperty("templateName")
  private String templateName;

  @JsonProperty("officeId")
  private int officeId;

  @JsonProperty("timeInterval")
  private Integer timeInterval;

  /**
   * The template ID
   *
   * @documentationExample 1
   *
   * @return Template ID
   */
  public int getTemplateId() {
    return templateId;
  }

  public void setTemplateId(int templateId) {
    this.templateId = templateId;
  }

  /**
   * The template name
   *
   * @documentationExample Thursday and Friday Schedule
   *
   * @return Template name
   */
  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  /**
   * The ID of the office object associated with this Availability Template DTO
   *
   * @documentationExample 1
   *
   * @return Office ID
   */
  public int getOfficeId() {
    return officeId;
  }

  public void setOfficeId(int officeId) {
    this.officeId = officeId;
  }

  /**
   * Length of time in minutes
   *
   * @documentationExample 15
   *
   * @return Length of time
   */
  public Integer getTimeInterval() {
    return timeInterval;
  }

  public void setTimeInterval(Integer timeInterval) {
    this.timeInterval = timeInterval;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 23 * hash + this.templateId;
    hash = 23 * hash + Objects.hashCode(this.templateName);
    hash = 23 * hash + this.officeId;
    hash = 23 * hash + Objects.hashCode(this.timeInterval);
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
    final AvailabilityTemplateDto other = (AvailabilityTemplateDto) obj;
    if (this.templateId != other.templateId) {
      return false;
    }
    if (this.officeId != other.officeId) {
      return false;
    }
    if (!Objects.equals(this.templateName, other.templateName)) {
      return false;
    }
    if (!Objects.equals(this.timeInterval, other.timeInterval)) {
      return false;
    }
    return true;
  }

}
