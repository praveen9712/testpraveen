
package com.qhrtech.emr.restapi.models.dto.converter;

import com.qhrtech.emr.restapi.models.dto.medicalhistory.ReactionCode;
import org.dozer.DozerConverter;

public class ReactionCodeConverter extends DozerConverter<String, ReactionCode> {

  public ReactionCodeConverter() {
    super(String.class, ReactionCode.class);
  }

  @Override
  public ReactionCode convertTo(String source, ReactionCode destination) {
    return ReactionCode.lookup(source);
  }

  @Override
  public String convertFrom(ReactionCode source, String destination) {
    return source == null ? null : source.getCode();
  }
}
