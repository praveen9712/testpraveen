
package com.qhrtech.emr.restapi.models.dto.converter;

import java.math.BigDecimal;
import org.dozer.DozerConverter;

public class BigDecimalConverter extends DozerConverter<BigDecimal, BigDecimal> {

  public BigDecimalConverter() {
    super(BigDecimal.class, BigDecimal.class);
  }

  @Override
  public BigDecimal convertTo(BigDecimal source, BigDecimal destination) {
    if (source == null) {
      return null;
    }
    return getWithTrimmedTrailingZeros(source);

  }

  @Override
  public BigDecimal convertFrom(BigDecimal source, BigDecimal destination) {
    if (source == null) {
      return null;
    }
    return getWithTrimmedTrailingZeros(source);
  }

  private BigDecimal getWithTrimmedTrailingZeros(BigDecimal source) {
    String s = source.toString();
    s = s.indexOf(".") < 0 ? s
        : s.replaceAll("0*$", "")
            .replaceAll("\\.$", "");
    return new BigDecimal(s);
  }
}
