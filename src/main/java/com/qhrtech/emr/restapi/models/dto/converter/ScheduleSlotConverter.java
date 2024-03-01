
package com.qhrtech.emr.restapi.models.dto.converter;

import com.qhrtech.emr.accuro.model.scheduling.ScheduleSlot;
import com.qhrtech.emr.restapi.models.dto.ScheduleSlotDto;
import org.dozer.DozerConverter;

/**
 * Handles conversion between ScheduleSlot and ScheduleSlotDto objects.
 *
 * @author jesse.pasos
 */
public class ScheduleSlotConverter extends DozerConverter<ScheduleSlot, ScheduleSlotDto> {

  public ScheduleSlotConverter() {
    super(ScheduleSlot.class, ScheduleSlotDto.class);
  }

  @Override
  public ScheduleSlotDto convertTo(ScheduleSlot source,
      ScheduleSlotDto destination) {

    if (source == null) {
      return null;
    }

    ScheduleSlotDto slot = new ScheduleSlotDto();

    slot.setDate(source.getDate());
    slot.setSubColumn(source.getSubColumn());
    slot.setStartTime(source.getStartTime());
    slot.setEndTime(source.getEndTime());
    slot.setProviderId(source.getProviderId());
    slot.setResourceId(source.getResourceId());

    return slot;
  }

  @Override
  public ScheduleSlot convertFrom(ScheduleSlotDto source,
      ScheduleSlot destination) {

    if (source == null) {
      return null;
    }

    return new ScheduleSlot(source.getDate(),
        source.getSubColumn(),
        source.getStartTime(),
        source.getEndTime(),
        source.getProviderId(),
        source.getResourceId());
  }

}
