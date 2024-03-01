
package com.qhrtech.emr.restapi.models.dto.converter;

import com.qhrtech.emr.restapi.models.dto.medicalhistory.LifeStageType;
import org.dozer.DozerConverter;

public class LifeStageTypeConverter extends DozerConverter<String, LifeStageType> {

  public LifeStageTypeConverter() {
    super(String.class, LifeStageType.class);
  }

  @Override
  public LifeStageType convertTo(String source, LifeStageType destination) {
    return LifeStageType.lookup(source);
  }

  @Override
  public String convertFrom(LifeStageType source, String destination) {
    return source == null ? null : source.getDescription();
  }
}
