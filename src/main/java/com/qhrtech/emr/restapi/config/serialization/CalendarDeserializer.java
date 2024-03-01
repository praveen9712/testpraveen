
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.accuro.model.time.CalendarPrecision;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarDeserializer extends AccuroCalendarBasedDeserializer<Calendar>
    implements ContextualDeserializer {

  private boolean dateOnly;

  public CalendarDeserializer() {
    super(Calendar.class);
  }

  protected CalendarDeserializer(boolean dateOnly, TimeZone serializationTimeZone) {
    super(Calendar.class, serializationTimeZone);
    this.dateOnly = dateOnly;
  }

  @Override
  public Calendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    AccuroCalendar cal = deserializeImpl(jp);
    if (cal == null) {
      return null;
    }

    if (dateOnly && cal.getPrecision().compareTo(CalendarPrecision.Day) > 0) {
      throw new JsonParseException(jp, "Field only supports dates, not timestamps.");
    }

    return AccuroCalendar.unwrap(cal);
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
    if (property != null) {
      JsonFormat.Value format =
          ctxt.getAnnotationIntrospector().findFormat(property.getMember());
      if (format != null) {
        boolean useDateOnly = false;
        String pattern = format.getPattern();
        if (pattern != null && !pattern.isEmpty()) {
          useDateOnly = pattern.equalsIgnoreCase("date") || pattern.equals("yyyy-MM-dd");
        }

        TimeZone targetTimezone = format.getTimeZone();
        if (targetTimezone != null || useDateOnly) {
          return new CalendarDeserializer(useDateOnly, targetTimezone);
        }
      }
    }
    return this;
  }
}
