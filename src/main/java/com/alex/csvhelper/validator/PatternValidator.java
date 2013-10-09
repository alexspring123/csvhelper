package com.alex.csvhelper.validator;

import com.alex.csvhelper.annotation.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-6-3
 * Time: 下午3:00
 * To change this template use File | Settings | File Templates.
 */
public class PatternValidator implements Validator<Pattern> {
  @Override
  public void validate(Pattern annotation, Object value) throws ValidateException {
    if (annotation == null || value == null)
      return;

    if (value instanceof String) {
      if (!java.util.regex.Pattern.matches(annotation.value(), (String) value)) {
        if (StringUtils.isBlank(annotation.description()))
          throw new ValidateException("格式错误，期望\"" + annotation.value() + "\"，实际\"。");
        else
          throw new ValidateException("格式错误，期望\"" + annotation.description() + "\"。");
      }
    }
  }
}
