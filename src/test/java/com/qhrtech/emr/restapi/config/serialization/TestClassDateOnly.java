
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Calendar;

class TestClassDateOnly {

  @JsonFormat(pattern = "date")
  private Calendar value;

  TestClassDateOnly() {
  }

  public TestClassDateOnly(Calendar value) {
    this.value = value;
  }

  public Calendar getValue() {
    return value;
  }

  public void setValue(Calendar value) {
    this.value = value;
  }
}
