package com.alex.csvhelper.validator;

import com.alex.csvhelper.annotation.Max;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-6-3
 * Time: 下午2:59
 * To change this template use File | Settings | File Templates.
 */
public class MaxValidator implements Validator<Max> {
  @Override
  public void validate(Max annotation, Object value) throws ValidateException {
    if (annotation == null || value == null)
      return;

    BigDecimal dv = toBigDecimal(value);
    int result = dv.compareTo(BigDecimal.valueOf(annotation.value()));
    if (annotation.allowEqual()) {
      if (result > 0)
        throw new ValidateException("必须小于" + annotation.value() + "，实际" + dv + "。");
    } else {
      if (result >= 0)
        throw new ValidateException("必须大于等于" + annotation.value() + "，实际" + dv + "。");
    }
  }

  private BigDecimal toBigDecimal(Object value) {
    if (value instanceof Double || value instanceof Float) {
      return BigDecimal.valueOf(((Number) value).doubleValue());
    } else if (value instanceof BigInteger) {
      return BigDecimal.valueOf(((BigInteger) value).doubleValue());
    } else if (value instanceof BigDecimal) {
      return (BigDecimal) value;
    } else if (value instanceof Number) {
      return BigDecimal.valueOf(((Number) value).longValue());
    } else
      return null;
  }
}
