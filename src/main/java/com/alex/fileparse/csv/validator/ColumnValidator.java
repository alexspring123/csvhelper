package com.alex.fileparse.csv.validator;

import com.alex.fileparse.csv.annotation.Column;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-30
 * Time: 下午10:43
 * To change this template use File | Settings | File Templates.
 */
public class ColumnValidator extends Validator {
    private Column annotation;

    public ColumnValidator(Annotation annotation) {
        if (annotation instanceof Column) {
            this.annotation = (Column) annotation;
        } else
            throw new RuntimeException("指定的参数不是" + Column.class.getName()
                    + "，初始化" + ColumnValidator.class.getName() + "失败。");
    }

    @Override
    public String validate(Object value) {
        StringBuilder sb = new StringBuilder();
        String errorMsg = validateNullAble(value);
        if (errorMsg != null)
            sb.append(errorMsg);
        errorMsg = validateLength(value);
        if (errorMsg != null)
            sb.append(errorMsg);
        errorMsg = validatePrecision(value);
        if (errorMsg != null)
            sb.append(errorMsg);
        errorMsg = validateScale(value);
        if (errorMsg != null)
            sb.append(errorMsg);

        if (sb.length() > 0)
            return sb.toString();
        else
            return null;
    }

    private String validateNullAble(Object value) {
        if (value == null && annotation.nullable() == false)
            return "不能为空。";
        else
            return null;
    }

    private String validateLength(Object value) {
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

    private String validatePrecision(Object value) {
        if (value == null)
            return null;
        if (value instanceof BigDecimal) {
            BigDecimal decimal = (BigDecimal) value;
            int expectPrecision = annotation.precision();
            int actualPrecision = decimal.precision();
            if (actualPrecision > expectPrecision)
                return "总位数超长，期望" + expectPrecision + "，实际" + actualPrecision + "。";
        }
        return null;
    }

    private String validateScale(Object value) {
        if (value == null)
            return null;
        if (value instanceof BigDecimal) {
            BigDecimal decimal = (BigDecimal) value;
            int expectScale = annotation.scale();
            int actualScale = decimal.scale();
            if (actualScale > expectScale)
                return "小数精度超长，期望" + expectScale + "，实际" + actualScale + "。";
        }
        return null;
    }
}
