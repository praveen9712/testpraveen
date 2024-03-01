
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Calendar;

class TestClassTimeZone {

  @JsonFormat(timezone = "UTC")
  private Calendar value;

  TestClassTimeZone() {
  }

  public TestClassTimeZone(Calendar value) {
    this.value = value;
  }

  public Calendar getValue() {
    return value;
  }

  public void setValue(Calendar value) {
    this.value = value;
  }
}
