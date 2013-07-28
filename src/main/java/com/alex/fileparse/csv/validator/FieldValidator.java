package com.alex.fileparse.csv.validator;

import com.alex.fileparse.csv.CsvReadException;
import com.alex.fileparse.csv.annotation.*;

import java.lang.Boolean;
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
    public static RoundingMode ROUNDINGMODE = RoundingMode.HALF_UP;

    public ValidatorResult validate(Field field, String content) {
        ValidatorResult result = new ValidatorResult();
        Object value = null;
        try {
            value = getValue(field, content);
            result.setValue(value);
        } catch (Exception e) {
            result.setErrorMsg(e.getMessage());
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
        result.setIsvalid(result.getErrorMsg() == null);
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
                String pattern = "yyyy-MM-dd";
                if (pattern != null)
                    pattern = patternAnnotion.value();
                typeDescription = "日期(" + pattern + ")";
                DateFormat dateFormat = new SimpleDateFormat(pattern);
                return dateFormat.parse(content);
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
