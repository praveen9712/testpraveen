
package com.qhrtech.emr.restapi.models.dto.converter;

import java.util.Calendar;
import org.dozer.DozerConverter;

public class CalendarValidateConverter extends DozerConverter<Calendar, Calendar> {

  public CalendarValidateConverter() {
    super(Calendar.class, Calendar.class);
  }

  @Override
  public Calendar convertTo(Calendar source, Calendar destination) {
    if (source == null) {
      return null;
    }
    return convertFrom(source, destination);
  }

  @Override
  public Calendar convertFrom(Calendar source, Calendar destination) {
    if (source == null) {
      return null;
    }

    // Check if firstDayOfWeek is valida
    int firstDayOfWeek = source.getFirstDayOfWeek();
    if (firstDayOfWeek > Calendar.SATURDAY || firstDayOfWeek < Calendar.SUNDAY) {
      source.setFirstDayOfWeek(Calendar.SUNDAY);
    }
    int minDaysInFirstWeek = source.getMinimalDaysInFirstWeek();
    if (minDaysInFirstWeek > 7 || minDaysInFirstWeek < 1) {
      source.setMinimalDaysInFirstWeek(1);
    }
    Calendar cal = (Calendar) source.clone();
    cal.setTimeZone(source.getTimeZone());
    return cal;
  }

}
