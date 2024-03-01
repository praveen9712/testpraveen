
package com.qhrtech.emr.restapi.models.dto.converter;

import org.dozer.DozerConverter;
import org.joda.time.LocalDate;

public class LocalDateConverter extends DozerConverter<LocalDate, LocalDate> {

  public LocalDateConverter() {
    super(LocalDate.class, LocalDate.class);
  }

  @Override
  public LocalDate convertTo(LocalDate source, LocalDate destination) {
    if (source == null) {
      return null;
    }
    return new LocalDate(source);
  }

  @Override
  public LocalDate convertFrom(LocalDate source, LocalDate destination) {
    if (source == null) {
      return null;
    }
    return new LocalDate(source);
  }
}
