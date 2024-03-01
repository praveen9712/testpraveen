
package com.qhrtech.emr.restapi.models.dto.tasks;

import java.util.Arrays;
import java.util.Objects;

public enum Priority {
  Normal("Normal", 1),
  Urgent("Urgent", 2),
  Very_Urgent("Very Urgent", 3);

  private final int id;
  private final String displayName;

  Priority(String display, int id) {
    this.displayName = display;
    this.id = id;
  }

  Priority lookup(int id) {
    return Arrays.stream(values())
        .filter(c -> Objects.equals(c.id, id))
        .findFirst()
        .orElse(null);
  }

  public int getId() {
    return id;
  }

  public String getDisplayName() {
    return displayName;
  }
}
