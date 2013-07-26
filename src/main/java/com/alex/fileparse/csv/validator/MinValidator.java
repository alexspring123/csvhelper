package com.alex.fileparse.csv.validator;

import com.alex.fileparse.csv.annotation.Min;

import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-6-3
 * Time: 下午3:00
 * To change this template use File | Settings | File Templates.
 */
public class MinValidator extends Validator {
    private Min annotation;

    public MinValidator(Annotation annotation) {
        if (annotation instanceof Min) {
            this.annotation = (Min) annotation;
        } else
            throw new RuntimeException("指定的参数不是" + Min.class.getName()
                    + "，初始化" + MinValidator.class.getName() + "失败。");
    }

    @Override
    public String validate(Object value) {
        if (value == null)
            return null;
        if (value instanceof Number) {
            double expect = annotation.value();
            double actual = ((Number) value).doubleValue();
            if (actual < expect)
                return "低于最小值，期望" + actual + "，实际" + expect + "。";
            else if (actual == expect && annotation.allowEqual() == false)
                return "低于最小值，期望" + actual + "(不允许相等)，实际" + expect + "。";
        }
        return null;
    }
}
