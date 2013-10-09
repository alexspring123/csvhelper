package com.alex.csvhelper.validator;

import com.alex.csvhelper.annotation.Enumeration;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-7-26
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
public class EnumerationValidator implements Validator<Enumeration> {

  @Override
  public void validate(Enumeration annotation, Object value) throws ValidateException {
    if (annotation == null || value == null)
      return;

    if (value instanceof String) {
      String[] enumValues = annotation.values();
      if (Arrays.asList(enumValues).contains(value) == false)
        throw new ValidateException("只能填写 " + StringUtils.join(enumValues, ','));
    }
  }
}
