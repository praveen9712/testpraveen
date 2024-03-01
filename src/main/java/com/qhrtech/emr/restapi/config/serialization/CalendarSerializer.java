
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.qhrtech.emr.accuro.utils.time.TimeFormatUtil;
import com.qhrtech.emr.accuro.utils.time.TimeUtil;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarSerializer extends StdScalarSerializer<Calendar>
    implements ContextualSerializer {

  private boolean dateOnly;
  private TimeZone targetTimeZone;

  public CalendarSerializer() {
    this(false, null);
  }

  protected CalendarSerializer(boolean dateOnly, TimeZone targetTimeZone) {
    super(Calendar.class);
    this.dateOnly = dateOnly;
    this.targetTimeZone = targetTimeZone;
  }

  @Override
  public void serialize(Calendar value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
    if (value == null) {
      jgen.writeNull();
      return;
    }

    TimeZone currentTz = (targetTimeZone == null ? null : TimeUtil.getThreadTimeZone());
    try {
      if (targetTimeZone != null) {
        TimeUtil.setThreadTimeZone(targetTimeZone);
      }

      jgen.writeString(TimeFormatUtil.formatDate(value,
          (dateOnly ? "yyyy-MM-dd" : "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ")));

    } finally {
      if (currentTz != null) {
        TimeUtil.setThreadTimeZone(currentTz);
      } else if (targetTimeZone != null) {
        TimeUtil.clearThreadTimeZone();
      }
    }
  }

  @Override
  public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
    if (property != null) {
      JsonFormat.Value format =
          prov.getAnnotationIntrospector().findFormat(property.getMember());
      if (format != null) {
        boolean useDateOnly = false;
        String pattern = format.getPattern();
        if (pattern != null && !pattern.isEmpty()) {
          useDateOnly = pattern.equalsIgnoreCase("date") || pattern.equals("yyyy-MM-dd");
        }

        TimeZone targetTimezone = format.getTimeZone();
        if (targetTimezone != null || useDateOnly) {
          return new CalendarSerializer(useDateOnly, targetTimezone);
        }
      }
    }
    return this;
  }
}
