package com.alex.fileparse.csv.validator;

import com.alex.fileparse.csv.annotation.Min;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-6-3
 * Time: 下午3:00
 * To change this template use File | Settings | File Templates.
 */
public class MinValidator implements Validator<Min> {
    @Override
    public String validate(Min annotation, Object value) {
        if (annotation == null || value == null)
            return null;

        BigDecimal dv = toBigDecimal(value);
        int result = dv.compareTo(BigDecimal.valueOf(annotation.value()));
        if (annotation.allowEqual()) {
            if (result < 0)
                return "小于最小值，期望" + annotation.value() + "，实际" + dv + "。";
        } else {
            if (result >= 0)
                return "小于最小值，期望" + annotation.value() + "(不允许相等)，实际" + dv + "。";
        }
        return null;
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
