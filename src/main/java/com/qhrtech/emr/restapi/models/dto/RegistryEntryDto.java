
package com.qhrtech.emr.restapi.models.dto;

import java.util.Objects;
import java.util.UUID;

public class RegistryEntryDto {


  private UUID uuid;
  private RegistryType type;
  private long id;
  private boolean dirty;

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public RegistryType getType() {
    return type;
  }

  public void setType(RegistryType type) {
    this.type = type;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegistryEntryDto that = (RegistryEntryDto) o;
    return id == that.id && dirty == that.dirty && uuid.equals(that.uuid) && type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, type, id, dirty);
  }

}
