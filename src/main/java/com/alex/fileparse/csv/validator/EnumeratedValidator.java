package com.alex.fileparse.csv.validator;

import com.alex.fileparse.csv.annotation.Enumerated;

import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-6-3
 * Time: 下午2:58
 * To change this template use File | Settings | File Templates.
 */
public class EnumeratedValidator extends Validator {
    private Enumerated annotation;

    public EnumeratedValidator(Annotation annotation) {
        if (annotation instanceof Enumerated) {
            this.annotation = (Enumerated) annotation;
        } else
            throw new RuntimeException("指定的参数不是" + Enumerated.class.getName()
                    + "，初始化" + EnumeratedValidator.class.getName() + "失败。");
    }

    @Override
    public String  validate(Object value) {
        // do nothing
        return null;
    }
}
