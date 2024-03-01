
package com.qhrtech.emr.restapi.models.dto.converter;

import com.optimedirect.cypher.Hex;
import org.dozer.CustomConverter;

public class HashByteToHexConverter implements CustomConverter {

  @Override
  public Object convert(Object destination, Object source, Class<?> destinationClass,
      Class<?> sourceClass) {

    if (source == null) {
      return null;
    }
    byte[] hashValue = Hex.fromHex((String) source);
    return hashValue;
  }
}
