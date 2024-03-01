
package com.qhrtech.emr.restapi.models.dto.converter;

import org.dozer.DozerConverter;
import org.joda.time.LocalDateTime;

public class LocalDateTimeConverter extends DozerConverter<LocalDateTime, LocalDateTime> {

  public LocalDateTimeConverter() {
    super(LocalDateTime.class, LocalDateTime.class);
  }

  @Override
  public LocalDateTime convertTo(LocalDateTime source, LocalDateTime destination) {
    return (null == source) ? null : new LocalDateTime(source);
  }

  @Override
  public LocalDateTime convertFrom(LocalDateTime source, LocalDateTime destination) {
    return (null == source) ? null : new LocalDateTime(source);
  }
}
