package com.alex.csvhelper.util;

import com.alex.csvhelper.CsvBean;
import com.alex.csvhelper.CsvReadException;
import com.alex.csvhelper.ProcessResult;
import com.alex.csvhelper.annotation.Column;
import com.alex.csvhelper.annotation.Pattern;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-9-30
 * Time: 上午10:07
 * To change this template use File | Settings | File Templates.
 */
public class CsvBeanInjector<T extends CsvBean> {
  public static final RoundingMode ROUNDINGMODE = RoundingMode.HALF_UP;
  public static final DateFormat DEFAULT_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
  public static final String DEFAULT_DATEFORMAT_DESCRIPTION = "必须是\"年-月-日\"格式。";

  /**
   * 将values注入到对象csvBean中
   *
   * @param csvBean 需要注入的对象
   * @param values  需要注入的数据
   */
  public void inject(T csvBean, Map<Field, String> values) throws CsvReadException {
    if (csvBean == null || values == null || values.isEmpty())
      return;

    for (Map.Entry<Field, String> entry : values.entrySet()) {
      Field field = entry.getKey();
      String content = entry.getValue();

      Object value = null;
      try {
        value = convert(content, field);
      } catch (Exception e) {
        String caption = ReflectUtil.getFieldCaption(field);
        csvBean.setResult(ProcessResult.failed);
        csvBean.addMessage(caption + e.getMessage());
      }

      if (value != null)
        injectField(csvBean, field, value);
    }
  }

  private Object convert(String content, Field field) throws CsvReadException {
    if (content == null || "".equals(content))
      return null;

    Class fieldType = field.getType();
    String typeDescription = null;
    try {
      if (fieldType == String.class) {
        typeDescription = "字符串";
        return content;
      } else if (fieldType == Date.class) {
        Pattern patternAnnotion = field.getAnnotation(Pattern.class);
        if (patternAnnotion == null) {
          typeDescription = DEFAULT_DATEFORMAT_DESCRIPTION;
          DEFAULT_DATEFORMAT.setLenient(false);
          return DEFAULT_DATEFORMAT.parse(content);
        } else {
          if ("".equals(patternAnnotion.description())) {
            typeDescription = "日期(" + patternAnnotion.value() + ")";
          } else {
            typeDescription = "日期(" + patternAnnotion.description() + ")";
          }
          DateFormat dateFormat = new SimpleDateFormat(patternAnnotion.value());
          dateFormat.setLenient(false);
          return dateFormat.parse(content);
        }
      } else if (fieldType == BigDecimal.class) {
        typeDescription = "数字";
        return getBigDecimal(field, content);
      } else if (fieldType == Integer.class) {
        typeDescription = "数字";
        return Integer.valueOf(getBigDecimal(field, content).intValue());
      } else if (fieldType == BigInteger.class) {
        typeDescription = "数字";
        return BigInteger.valueOf(getBigDecimal(field, content).longValue());
      } else if (fieldType == int.class) {
        typeDescription = "数字";
        return getBigDecimal(field, content).intValue();
      } else if (fieldType == long.class) {
        typeDescription = "数字";
        return getBigDecimal(field, content).longValue();
      } else if (fieldType == Long.class) {
        typeDescription = "数字";
        return Long.valueOf(getBigDecimal(field, content).longValue());
      } else if (fieldType == float.class) {
        typeDescription = "数字";
        return getBigDecimal(field, content).floatValue();
      } else if (fieldType == Float.class) {
        typeDescription = "数字";
        return Float.valueOf(getBigDecimal(field, content).floatValue());
      } else if (fieldType == double.class) {
        typeDescription = "数字";
        return getBigDecimal(field, content).doubleValue();
      } else if (fieldType == Double.class) {
        typeDescription = "数字";
        return Double.valueOf(getBigDecimal(field, content).doubleValue());
      } else if (fieldType == short.class) {
        typeDescription = "数字";
        return getBigDecimal(field, content).shortValue();
      } else if (fieldType == Short.class) {
        typeDescription = "数字";
        return Short.valueOf(getBigDecimal(field, content).shortValue());
      } else if (fieldType == boolean.class) {
        boolean value = true;
        try {
          value = getBoolean(field, content);
        } catch (CsvReadException e) {
          typeDescription = e.getMessage();
        }
        return value;
      } else if (fieldType == Boolean.class) {
        boolean value = true;
        try {
          value = getBoolean(field, content);
        } catch (CsvReadException e) {
          typeDescription = e.getMessage();
        }
        return Boolean.valueOf(value);
      } else if (fieldType == char.class) {
        return content.charAt(0);
      }
      throw new CsvReadException("不支持的数据类型" + fieldType.getName());
    } catch (ParseException e) {
      throw new CsvReadException("数据格式错误，需要" + typeDescription);
    } catch (RuntimeException e) {
      throw new CsvReadException("数据格式错误，需要" + typeDescription);
    }
  }

  public void injectField(Object bean, Field field, Object fieldValue) throws CsvReadException {
    String setterName = getSetter(field.getName());
    Method setMethod;
    try {
      setMethod = bean.getClass().getMethod(setterName, field.getType());
    } catch (NoSuchMethodException e) {
      throw new CsvReadException("类" + bean.getClass().getName() + "中不存在方法" + setterName + "。", e);
    }

    try {
      setMethod.invoke(bean, fieldValue);
    } catch (IllegalAccessException e) {
      throw new CsvReadException("调用类" + bean.getClass().getName() + "中方法" + setterName + "出错。", e);
    } catch (InvocationTargetException e) {
      throw new CsvReadException("调用类" + bean.getClass().getName() + "中方法" + setterName + "出错。", e);
    }
  }

  private String getSetter(String fieldName) {
    if (StringUtils.isBlank(fieldName))
      return null;
    String fieldFirstUpperChar = fieldName.substring(0, 1).toUpperCase();
    return "set" + fieldFirstUpperChar + fieldName.substring(1);
  }

  private BigDecimal getBigDecimal(Field field, String content) {
    Column columnAnnotion = field.getAnnotation(Column.class);
    BigDecimal value = new BigDecimal(content, new MathContext(columnAnnotion.precision(), ROUNDINGMODE));
    value.setScale(columnAnnotion.scale(), ROUNDINGMODE);
    return value;
  }

  private boolean getBoolean(Field field, String content) throws CsvReadException {
    com.alex.csvhelper.annotation.Boolean booleanAnnotion = field.getAnnotation(com.alex.csvhelper.annotation.Boolean.class);
    String trueStr = null;
    String falseStr = null;
    if (booleanAnnotion != null) {
      trueStr = booleanAnnotion.trueStr();
      falseStr = booleanAnnotion.falseStr();
    }
    if (!content.equals(trueStr) && !content.equals(falseStr)) {
      throw new CsvReadException("只能填写\"" + trueStr + "\"或\"" + falseStr + "\"。");
    }
    return content.equals(trueStr);
  }
}
