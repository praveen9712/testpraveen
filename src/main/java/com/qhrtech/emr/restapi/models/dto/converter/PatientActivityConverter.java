
package com.qhrtech.emr.restapi.models.dto.converter;

import com.qhrtech.emr.accuro.model.logging.PatientActivity;
import com.qhrtech.emr.restapi.models.dto.PatientActivityDto;
import com.qhrtech.emr.restapi.models.dto.PatientActivityType;
import org.dozer.DozerConverter;

/**
 *
 * @author jesse.pasos
 */
public class PatientActivityConverter extends DozerConverter<PatientActivity, PatientActivityDto> {

  public PatientActivityConverter() {
    super(PatientActivity.class, PatientActivityDto.class);
  }

  @Override
  public PatientActivityDto convertTo(PatientActivity source, PatientActivityDto destination) {
    if (source == null) {
      return null;
    }
    PatientActivityDto out = new PatientActivityDto();
    out.setDescription(source.getDescription());
    out.setPatientId(source.getPatientId());
    out.setType(PatientActivityType.lookup(source.getType().name()));
    out.setRecordId(source.getRecordId());
    out.setTimeUtc(source.getTimeUtc());
    return out;
  }

  @Override
  public PatientActivity convertFrom(PatientActivityDto source, PatientActivity destination) {
    if (source == null) {
      return null;
    }
    return new PatientActivity(source.getPatientId(),
        source.getRecordId(),
        source.getTimeUtc(),
        com.qhrtech.emr.accuro.model.logging.PatientActivityType.lookup(source.getType().name()),
        source.getDescription());

  }

}
