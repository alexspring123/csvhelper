package com.alex.fileparse.csv.validator;

import com.alex.fileparse.csv.CsvReadException;
import com.alex.fileparse.csv.annotation.Column;
import com.alex.fileparse.csv.annotation.Pattern;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-30
 * Time: 下午10:14
 * To change this template use File | Settings | File Templates.
 */
public class FieldValidator {
    public static final RoundingMode ROUNDINGMODE = RoundingMode.HALF_UP;
    public static final DateFormat DEFAULT_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final String DEFAULT_DATEFORMAT_DESCRIPTION = "必须是\"年-月-日\"格式。";

    public ValidatorResult validate(Field field, String content) {
        ValidatorResult result = new ValidatorResult();
        Object value = null;
        try {
            value = getValue(field, content);
            result.setValue(value);
        } catch (Exception e) {
            result.addErrorMgr(e.getMessage());
            return result;
        }

        for (Annotation annotation : field.getAnnotations()) {
            ValidatorClass validatorClass = annotation.annotationType().getAnnotation(ValidatorClass.class);
            if (validatorClass == null)
                continue;

            Validator validator = null;
            try {
                validator = getValidator(validatorClass);
            } catch (Exception e) {
                result.addErrorMgr(e.getMessage());
            }
            if (validator != null) {
                String errorMsg = validator.validate(annotation, value);
                if (errorMsg != null) {
                    result.addErrorMgr(errorMsg);
                }
            }
        }
        return result;
    }

    private Object getValue(Field field, String content) throws Exception {
        if (content == null || "".equals(content.toString()))
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

    private BigDecimal getBigDecimal(Field field, String content) {
        Column columnAnnotion = field.getAnnotation(Column.class);
        BigDecimal value = new BigDecimal(content, new MathContext(columnAnnotion.precision(), ROUNDINGMODE));
        value.setScale(columnAnnotion.scale(), ROUNDINGMODE);
        return value;
    }

    private boolean getBoolean(Field field, String content) throws CsvReadException {
        com.alex.fileparse.csv.annotation.Boolean booleanAnnotion = field.getAnnotation(com.alex.fileparse.csv.annotation.Boolean.class);
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
