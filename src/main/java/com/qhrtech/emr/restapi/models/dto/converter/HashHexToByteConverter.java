
package com.qhrtech.emr.restapi.models.dto.converter;

import com.optimedirect.cypher.Hex;
import org.dozer.CustomConverter;

public class HashHexToByteConverter implements CustomConverter {


  @Override
  public Object convert(Object destination, Object source, Class<?> destinationClass,
      Class<?> sourceClass) {

    if (source == null) {
      return null;
    }
    String hashValue = Hex.toHex((byte[]) source);
    return hashValue;
  }

}
