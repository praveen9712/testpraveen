
package com.qhrtech.emr.restapi.models.dto.converter;

import com.qhrtech.emr.restapi.models.dto.medicalhistory.EyeCode;
import org.dozer.DozerConverter;

public class EyeCodeConverter extends DozerConverter<String, EyeCode> {

  public EyeCodeConverter() {
    super(String.class, EyeCode.class);
  }

  @Override
  public EyeCode convertTo(String source, EyeCode destination) {
    return EyeCode.lookup(source);
  }

  @Override
  public String convertFrom(EyeCode source, String destination) {
    return source == null ? "" : source.getAbbreviation();
  }
}
