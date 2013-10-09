package com.alex.csvhelper.validator;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-8-1
 * Time: 下午4:23
 * To change this template use File | Settings | File Templates.
 */
public class BooleanValidator implements Validator<com.alex.csvhelper.annotation.Boolean> {
  @Override
  public void validate(com.alex.csvhelper.annotation.Boolean annotation, Object value) throws ValidateException {
    if (annotation == null || value == null)
      return;
  }
}
