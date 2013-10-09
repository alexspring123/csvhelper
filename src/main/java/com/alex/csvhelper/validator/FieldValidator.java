package com.alex.csvhelper.validator;

import com.alex.csvhelper.CsvBean;
import com.alex.csvhelper.ProcessResult;
import com.alex.csvhelper.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-30
 * Time: 下午10:14
 * To change this template use File | Settings | File Templates.
 */
public class FieldValidator {

  public void validate(CsvBean bean) {
    if (bean == null)
      return;

    Field[] fields = bean.getClass().getDeclaredFields();
    for (Field field : fields) {
      try {
        validateField(bean, field);
      } catch (Exception e) {
        String caption = ReflectUtil.getFieldCaption(field);
        bean.setResult(ProcessResult.failed);
        bean.addMessage(caption + e.getMessage());
      }
    }
  }

  private void validateField(CsvBean bean, Field field) throws IllegalAccessException, InstantiationException, ValidateException {
    if (bean == null || field == null)
      return;

    field.setAccessible(true);
    Object value = field.get(bean);

    for (Annotation annotation : field.getAnnotations()) {
      ValidatorClass validatorClass = annotation.annotationType().getAnnotation(ValidatorClass.class);
      if (validatorClass == null)
        continue;

      getValidator(validatorClass).validate(annotation, value);
    }
  }

  private Validator getValidator(ValidatorClass validatorClass) throws IllegalAccessException, InstantiationException {
    Class clazz = validatorClass.value();
    Validator validator = validatorCache.get(clazz.getName());
    if (validator == null) {
      validator = (Validator) clazz.newInstance();
      validatorCache.put(clazz.getName(), validator);
    }
    return validator;
  }

  private HashMap<String, Validator> validatorCache = new HashMap<String, Validator>();
}
