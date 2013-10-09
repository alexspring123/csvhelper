package com.alex.csvhelper.validator;

import com.alex.csvhelper.annotation.Column;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-30
 * Time: 下午10:43
 * To change this template use File | Settings | File Templates.
 */
public class ColumnValidator implements Validator<Column> {
  @Override
  public void validate(Column annotation, Object value) throws ValidateException {
    if (annotation == null)
      return;

    StringBuilder sb = new StringBuilder();
    String errorMsg = validateNullAble(annotation, value);
    if (errorMsg != null)
      sb.append(errorMsg);
    errorMsg = validateLength(annotation, value);
    if (errorMsg != null)
      sb.append(errorMsg);

    if (sb.length() > 0)
      throw new ValidateException(sb.toString());

  }

  private String validateNullAble(Column annotation, Object value) {
    if (value == null && annotation.nullable() == false)
      return "不能为空。";
    else
      return null;
  }

  private String validateLength(Column annotation, Object value) {
    if (value == null)
      return null;
    if (value instanceof String) {
      int expectLength = annotation.length();
      int actualLength = value.toString().length();
      if (actualLength > expectLength)
        return "字符超长，期望" + expectLength + "，实际" + actualLength + "。";
    }
    return null;
  }
}
