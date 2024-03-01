
package com.qhrtech.emr.restapi.models.validators;

import com.qhrtech.emr.restapi.models.dto.DocumentDto;
import com.qhrtech.emr.restapi.validators.CheckFrom;
import com.qhrtech.emr.restapi.validators.CheckReferrals;
import javax.validation.Valid;

@CheckReferrals
@CheckFrom
public class DocumentValidator {

  String type;


  @Valid
  DocumentDto documentDto;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public DocumentDto getDocumentDto() {
    return documentDto;
  }

  public void setDocumentDto(DocumentDto documentDto) {
    this.documentDto = documentDto;
  }

  @Override
  public String toString() {
    return "DocumentValidator{"
        + "type='" + type + '\''
        + ", documentDto=" + documentDto
        + '}';
  }

}
