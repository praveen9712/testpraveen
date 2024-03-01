
package com.qhrtech.emr.restapi.models.dto.converter;

import java.util.Calendar;
import java.util.TimeZone;
import org.dozer.DozerConverter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class CalendarConverter extends DozerConverter<DateTime, Calendar> {

  public CalendarConverter() {
    super(DateTime.class, Calendar.class);
  }

  @Override
  public Calendar convertTo(DateTime source, Calendar destination) {
    if (source == null) {
      return null;
    }

    Calendar cal = source.toGregorianCalendar();

    // Set the same timezone
    TimeZone timeZone = TimeZone.getTimeZone(source.getZone().getID());
    cal.setTimeZone(timeZone);

    return cal;
  }

  @Override
  public DateTime convertFrom(Calendar source, DateTime destination) {
    if (source == null) {
      return null;
    }
    TimeZone r = source.getTimeZone();
    DateTimeZone dtz = DateTimeZone.forID(r.getID());
    return new DateTime(source.getTimeInMillis(), dtz);
  }
}
