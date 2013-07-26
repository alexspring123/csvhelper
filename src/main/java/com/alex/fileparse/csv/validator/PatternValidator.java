package com.alex.fileparse.csv.validator;

import com.alex.fileparse.csv.annotation.Pattern;

import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-6-3
 * Time: 下午3:00
 * To change this template use File | Settings | File Templates.
 */
public class PatternValidator extends Validator {
    private Pattern annotation;

    public PatternValidator(Annotation annotation) {
        if (annotation instanceof Pattern) {
            this.annotation = (Pattern) annotation;
        } else
            throw new RuntimeException("指定的参数不是" + Pattern.class.getName()
                    + "，初始化" + PatternValidator.class.getName() + "失败。");
    }

    @Override
    public String validate(Object value) {
        if (value == null)
            return null;
        if (value instanceof String) {
            if (!java.util.regex.Pattern.matches(annotation.value(), (String) value))
                return "不符合规范，期望\"" + annotation.value() + "\"，实际\"" + value + "\"。";
        }
        return null;
    }
}
