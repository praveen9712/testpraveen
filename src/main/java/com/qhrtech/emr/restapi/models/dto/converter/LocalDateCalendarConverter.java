
package com.qhrtech.emr.restapi.models.dto.converter;

import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.accuro.utils.time.TimeUtil;
import com.qhrtech.emr.restapi.config.serialization.AccuroCalendarBasedDeserializer;
import java.util.Calendar;
import java.util.TimeZone;
import org.dozer.DozerConverter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LocalDateCalendarConverter extends DozerConverter<LocalDate, Calendar> {

  public LocalDateCalendarConverter() {
    super(LocalDate.class, Calendar.class);
  }

  @Override
  public Calendar convertTo(LocalDate localDate, Calendar calendar) {
    if (localDate == null) {
      return null;
    }

    DateTime localDateTime = localDate.toDateTimeAtStartOfDay();

    String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
    String dateTimeString = localDateTime.toString(formatter);
    AccuroCalendar parse =
        AccuroCalendarBasedDeserializer.parse(dateTimeString, TimeUtil.getTimeZone());
    return AccuroCalendar.unwrap(parse);
  }

  @Override
  public LocalDate convertFrom(Calendar calendar, LocalDate localDateTime) {
    if (calendar == null) {
      return null;
    }
    return LocalDate.fromCalendarFields(calendar);
  }
}
