
package com.qhrtech.emr.restapi.models.dto.repliform;

import com.qhrtech.emr.dataaccess.annotation.Model;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import java.io.Serializable;
import java.util.List;
import org.joda.time.LocalDateTime;

@Model
public final class PatientRepliformReportableDto implements Serializable {

  private int patientFormId;

  @DocumentationExample("2022-02-15 07:44:59.000")
  @TypeHint(String.class)
  private LocalDateTime dateOfService;

  private List<RepliformDataDto> repliformDataList;


  public int getPatientFormId() {
    return patientFormId;
  }

  public void setPatientFormId(int patientFormId) {
    this.patientFormId = patientFormId;
  }

  @DocumentationExample("2022-02-15 07:44:59.000")
  @TypeHint(String.class)
  public LocalDateTime getDateOfService() {
    return dateOfService;
  }

  public void setDateOfService(LocalDateTime dateOfService) {
    this.dateOfService = dateOfService;
  }

  public List<RepliformDataDto> getRepliformDataList() {
    return repliformDataList;
  }

  public void setRepliformDataList(
      List<RepliformDataDto> repliformDataList) {
    this.repliformDataList = repliformDataList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PatientRepliformReportableDto that = (PatientRepliformReportableDto) o;

    if (patientFormId != that.patientFormId) {
      return false;
    }
    if (!dateOfService.equals(that.dateOfService)) {
      return false;
    }
    return repliformDataList.equals(that.repliformDataList);
  }

  @Override
  public int hashCode() {
    int result = patientFormId;
    result = 31 * result + dateOfService.hashCode();
    result = 31 * result + repliformDataList.hashCode();
    return result;
  }
}
