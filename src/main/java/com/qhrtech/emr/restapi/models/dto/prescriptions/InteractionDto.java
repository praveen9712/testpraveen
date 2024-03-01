
package com.qhrtech.emr.restapi.models.dto.prescriptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.ScreenType;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.SeverityCode;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import org.joda.time.LocalDateTime;

@Schema(description = "Interaction data transfer object model")
public class InteractionDto {

  @JsonProperty("id")
  @Schema(description = "The interaction id", example = "1")
  private int id;

  @JsonProperty("prescriptionId")
  @Schema(description = "The prescription id of the prescription which has the interaction",
      example = "1")
  private int prescriptionId;

  @JsonProperty("nameSpace")
  @Schema(description = "The name space", example = "ACCURO")
  private String nameSpace;

  @JsonProperty("source")
  @Schema(description = "The source of the interaction", example = "\"5\" Strain Dophilus")
  private String source;

  @JsonProperty("severityCode")
  @Schema(description = "The severity code", example = "UN")
  private SeverityCode severityCode;

  @JsonProperty("severityText")
  @Schema(description = "The severity text", example = "UNKNOWN")
  private String severityText;

  @JsonProperty("screenType")
  @Schema(description = "The interaction screenType", example = "DAI")
  private ScreenType screenType;

  @JsonProperty("subType")
  @Schema(description = "The interaction sub screenType", example = "SAMPLE")
  private String subType;

  @JsonProperty("subTypeText")
  @Schema(description = "The text of the interaction sub screenType", example = "SAMPLE SUB TYPE")
  private String subTypeText;

  @JsonProperty("subCodeSystem")
  @Schema(description = "The sub code system", example = "SUBCODE")
  private String subCodeSystem;

  @JsonProperty("name")
  @Schema(description = "The name of the interaction",
      example = "\"5\" Strain Dophilus allergy conflicts with \"5\" Strain Dophilus")
  private String name;

  @JsonProperty("monographName")
  @Schema(description = "The monograph name", example = "SAMPLE MONOGRAPH")
  private String monographName;

  @JsonProperty("monographText")
  @Schema(description = "The monograph text", example = "THIS IS A SAMPLE MONOGRAPH")
  private String monographText;

  @JsonProperty("monographEffective")
  @Schema(description = "The effective date for the monograph", type = "string",
      example = "2018-07-13T00:00:00.000")
  private LocalDateTime monographEffective;

  @JsonProperty("minDosage")
  @Schema(description = "The minimum dosage", example = "1")
  private String minDosage;

  @JsonProperty("maxDosage")
  @Schema(description = "The maximum dosage", example = "0")
  private String maxDosage;

  @JsonProperty("dosageUnit")
  @Schema(description = "The dosage unit", example = "CAP")
  private String dosageUnit;

  @JsonProperty("reqestedDosage")
  @Schema(description = "The requested dosage", example = "1")
  private String reqestedDosage;

  @JsonProperty("reqestedUnit")
  @Schema(description = "The requested dosage unit", example = "CAP")
  private String reqestedUnit;

  @JsonProperty("systemSource")
  @Schema(description = "The system source", example = "SAMPLE SOURCE")
  private String systemSource;

  @JsonProperty("requiredManagement")
  @Schema(description = "The flag indicating if the dosage is required for the management",
      example = "false")
  private boolean requiredManagement;

  @JsonProperty("sourceId")
  @Schema(description = "The source id", example = "1")
  private String sourceId;

  @JsonProperty("interactionManagementDetails")
  @Schema(description = "A set of interaction management details")
  private Set<InteractionManagementDetailsDto> interactionManagementDetails;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * A prescription id of a prescription which has an interaction.
   *
   * @documentationExample 1
   *
   * @return
   */
  public int getPrescriptionId() {
    return prescriptionId;
  }

  public void setPrescriptionId(int prescriptionId) {
    this.prescriptionId = prescriptionId;
  }

  /**
   * A name space
   *
   * @documentationExample ACCURO
   *
   * @return
   */
  public String getNameSpace() {
    return nameSpace;
  }

  public void setNameSpace(String nameSpace) {
    this.nameSpace = nameSpace;
  }

  /**
   * A source of an interaction.
   *
   * @documentationExample "5" Strain Dophilus
   *
   * @return
   */
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  /**
   * A severity code.
   *
   * @documentationExample UN
   *
   * @return
   */
  public SeverityCode getSeverityCode() {
    return severityCode;
  }

  public void setSeverityCode(SeverityCode severityCode) {
    this.severityCode = severityCode;
  }

  /**
   * A severity text.
   *
   * @documentationExample UNKNOWN
   *
   * @return
   */
  public String getSeverityText() {
    return severityText;
  }

  public void setSeverityText(String severityText) {
    this.severityText = severityText;
  }

  /**
   * An interaction screenType.
   *
   * @documentationExample DAI
   *
   * @return
   */
  public ScreenType getScreenType() {
    return screenType;
  }

  public void setScreenType(ScreenType screenType) {
    this.screenType = screenType;
  }

  /**
   * An interaction sub screenType.
   *
   * @documentationExample SAMPLE
   *
   * @return
   */
  public String getSubType() {
    return subType;
  }

  public void setSubType(String subType) {
    this.subType = subType;
  }

  /**
   * A text of an interaction sub screenType.
   *
   * @documentationExample SAMPLE SUB TYPE
   *
   * @return
   */
  public String getSubTypeText() {
    return subTypeText;
  }

  public void setSubTypeText(String subTypeText) {
    this.subTypeText = subTypeText;
  }

  /**
   * A sub code system.
   *
   * @documentationExample SUBCODE
   *
   * @return
   */
  public String getSubCodeSystem() {
    return subCodeSystem;
  }

  public void setSubCodeSystem(String subCodeSystem) {
    this.subCodeSystem = subCodeSystem;
  }

  /**
   * A name of an interaction.
   *
   * @documentationExample "5" Strain Dophilus allergy conflicts with "5" Strain Dophilus
   *
   * @return
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * A monograph name.
   *
   * @documentationExample SAMPLE MONOGRAPH
   *
   * @return
   */
  public String getMonographName() {
    return monographName;
  }

  public void setMonographName(String monographName) {
    this.monographName = monographName;
  }

  /**
   * A monograph text.
   *
   * @documentationExample THIS IS A SAMPLE MONOGRAPH
   *
   * @return
   */
  public String getMonographText() {
    return monographText;
  }

  public void setMonographText(String monographText) {
    this.monographText = monographText;
  }

  /**
   * An effective date for a monograph.
   *
   * @return
   */
  @DocumentationExample("2018-07-13T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getMonographEffective() {
    return monographEffective;
  }

  public void setMonographEffective(LocalDateTime monographEffective) {
    this.monographEffective = monographEffective;
  }

  /**
   * A minimum dosage.
   *
   * @documentationExample 1
   *
   * @return
   */
  public String getMinDosage() {
    return minDosage;
  }

  public void setMinDosage(String minDosage) {
    this.minDosage = minDosage;
  }

  /**
   * A maximum dosage.
   *
   * @documentationExample 0
   *
   * @return
   */
  public String getMaxDosage() {
    return maxDosage;
  }

  public void setMaxDosage(String maxDosage) {
    this.maxDosage = maxDosage;
  }

  /**
   * A dosage unit.
   *
   * @documentationExample CAP
   *
   * @return
   */
  public String getDosageUnit() {
    return dosageUnit;
  }

  public void setDosageUnit(String dosageUnit) {
    this.dosageUnit = dosageUnit;
  }

  /**
   * A requested dosage.
   *
   * @documentationExample 1
   *
   * @return
   */
  public String getReqestedDosage() {
    return reqestedDosage;
  }

  public void setReqestedDosage(String reqestedDosage) {
    this.reqestedDosage = reqestedDosage;
  }

  /**
   * A requested dosage unit.
   *
   * @documentationExample CAP
   *
   * @return
   */
  public String getReqestedUnit() {
    return reqestedUnit;
  }

  public void setReqestedUnit(String reqestedUnit) {
    this.reqestedUnit = reqestedUnit;
  }

  /**
   * A system source
   *
   * @documentationExample SAMPLE SOURCE
   *
   * @return
   */
  public String getSystemSource() {
    return systemSource;
  }

  public void setSystemSource(String systemSource) {
    this.systemSource = systemSource;
  }

  /**
   * A flag if a dosage is required for a management.
   *
   * @documentationExample false
   *
   * @return
   */
  public boolean isRequiredManagement() {
    return requiredManagement;
  }

  public void setRequiredManagement(boolean requiredManagement) {
    this.requiredManagement = requiredManagement;
  }

  /**
   * A source id.
   *
   * @documentationExample 1
   *
   * @return
   */
  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  /**
   * A set of interaction management details.
   *
   * @return
   */
  public Set<InteractionManagementDetailsDto> getInteractionManagementDetails() {
    return interactionManagementDetails;
  }

  public void setInteractionManagementDetails(
      Set<InteractionManagementDetailsDto> interactionManagementDetails) {
    this.interactionManagementDetails = interactionManagementDetails;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof InteractionDto)) {
      return false;
    }

    InteractionDto that = (InteractionDto) o;

    if (getId() != that.getId()) {
      return false;
    }
    if (getPrescriptionId() != that.getPrescriptionId()) {
      return false;
    }
    if (isRequiredManagement() != that.isRequiredManagement()) {
      return false;
    }
    if (getNameSpace() != null ? !getNameSpace().equals(that.getNameSpace())
        : that.getNameSpace() != null) {
      return false;
    }
    if (getSource() != null ? !getSource().equals(that.getSource()) : that.getSource() != null) {
      return false;
    }
    if (getSeverityCode() != null ? !getSeverityCode().equals(that.getSeverityCode())
        : that.getSeverityCode() != null) {
      return false;
    }
    if (getSeverityText() != null ? !getSeverityText().equals(that.getSeverityText())
        : that.getSeverityText() != null) {
      return false;
    }
    if (getScreenType() != null ? !getScreenType().equals(that.getScreenType())
        : that.getScreenType() != null) {
      return false;
    }
    if (getSubType() != null ? !getSubType().equals(that.getSubType())
        : that.getSubType() != null) {
      return false;
    }
    if (getSubTypeText() != null ? !getSubTypeText().equals(that.getSubTypeText())
        : that.getSubTypeText() != null) {
      return false;
    }
    if (getSubCodeSystem() != null ? !getSubCodeSystem().equals(that.getSubCodeSystem())
        : that.getSubCodeSystem() != null) {
      return false;
    }
    if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
      return false;
    }
    if (getMonographName() != null ? !getMonographName().equals(that.getMonographName())
        : that.getMonographName() != null) {
      return false;
    }
    if (getMonographText() != null ? !getMonographText().equals(that.getMonographText())
        : that.getMonographText() != null) {
      return false;
    }
    if (getMonographEffective() != null ? !getMonographEffective()
        .equals(that.getMonographEffective()) : that.getMonographEffective() != null) {
      return false;
    }
    if (getMinDosage() != null ? !getMinDosage().equals(that.getMinDosage())
        : that.getMinDosage() != null) {
      return false;
    }
    if (getMaxDosage() != null ? !getMaxDosage().equals(that.getMaxDosage())
        : that.getMaxDosage() != null) {
      return false;
    }
    if (getDosageUnit() != null ? !getDosageUnit().equals(that.getDosageUnit())
        : that.getDosageUnit() != null) {
      return false;
    }
    if (getReqestedDosage() != null ? !getReqestedDosage().equals(that.getReqestedDosage())
        : that.getReqestedDosage() != null) {
      return false;
    }
    if (getReqestedUnit() != null ? !getReqestedUnit().equals(that.getReqestedUnit())
        : that.getReqestedUnit() != null) {
      return false;
    }
    if (getSystemSource() != null ? !getSystemSource().equals(that.getSystemSource())
        : that.getSystemSource() != null) {
      return false;
    }
    if (getSourceId() != null ? !getSourceId().equals(that.getSourceId())
        : that.getSourceId() != null) {
      return false;
    }
    return getInteractionManagementDetails() != null ? getInteractionManagementDetails()
        .equals(that.getInteractionManagementDetails())
        : that.getInteractionManagementDetails() == null;
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + getPrescriptionId();
    result = 31 * result + (getNameSpace() != null ? getNameSpace().hashCode() : 0);
    result = 31 * result + (getSource() != null ? getSource().hashCode() : 0);
    result = 31 * result + (getSeverityCode() != null ? getSeverityCode().hashCode() : 0);
    result = 31 * result + (getSeverityText() != null ? getSeverityText().hashCode() : 0);
    result = 31 * result + (getScreenType() != null ? getScreenType().hashCode() : 0);
    result = 31 * result + (getSubType() != null ? getSubType().hashCode() : 0);
    result = 31 * result + (getSubTypeText() != null ? getSubTypeText().hashCode() : 0);
    result = 31 * result + (getSubCodeSystem() != null ? getSubCodeSystem().hashCode() : 0);
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getMonographName() != null ? getMonographName().hashCode() : 0);
    result = 31 * result + (getMonographText() != null ? getMonographText().hashCode() : 0);
    result =
        31 * result + (getMonographEffective() != null ? getMonographEffective().hashCode() : 0);
    result = 31 * result + (getMinDosage() != null ? getMinDosage().hashCode() : 0);
    result = 31 * result + (getMaxDosage() != null ? getMaxDosage().hashCode() : 0);
    result = 31 * result + (getDosageUnit() != null ? getDosageUnit().hashCode() : 0);
    result = 31 * result + (getReqestedDosage() != null ? getReqestedDosage().hashCode() : 0);
    result = 31 * result + (getReqestedUnit() != null ? getReqestedUnit().hashCode() : 0);
    result = 31 * result + (getSystemSource() != null ? getSystemSource().hashCode() : 0);
    result = 31 * result + (isRequiredManagement() ? 1 : 0);
    result = 31 * result + (getSourceId() != null ? getSourceId().hashCode() : 0);
    result =
        31 * result + (getInteractionManagementDetails() != null ? getInteractionManagementDetails()
            .hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "InteractionDto{"
        + "id=" + id
        + ", prescriptionId=" + prescriptionId
        + ", nameSpace='" + nameSpace + '\''
        + ", source='" + source + '\''
        + ", severityCode='" + severityCode + '\''
        + ", severityText='" + severityText + '\''
        + ", screenType='" + screenType + '\''
        + ", subType='" + subType + '\''
        + ", subTypeText='" + subTypeText + '\''
        + ", subCodeSystem='" + subCodeSystem + '\''
        + ", name='" + name + '\''
        + ", monographName='" + monographName + '\''
        + ", monographText='" + monographText + '\''
        + ", monographEffective=" + monographEffective
        + ", minDosage='" + minDosage + '\''
        + ", maxDosage='" + maxDosage + '\''
        + ", dosageUnit='" + dosageUnit + '\''
        + ", reqestedDosage='" + reqestedDosage + '\''
        + ", reqestedUnit='" + reqestedUnit + '\''
        + ", systemSource='" + systemSource + '\''
        + ", requiredManagement=" + requiredManagement
        + ", sourceId='" + sourceId + '\''
        + ", interactionManagementDetails=" + interactionManagementDetails
        + '}';
  }
}
