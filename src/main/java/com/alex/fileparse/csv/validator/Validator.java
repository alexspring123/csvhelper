package com.alex.fileparse.csv.validator;

import com.alex.fileparse.csv.annotation.*;

import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-30
 * Time: 下午10:14
 * To change this template use File | Settings | File Templates.
 */
public abstract class Validator {
    public static Validator getInstance(Annotation annotation) {
        if (annotation instanceof Column)
            return new ColumnValidator(annotation);
        if (annotation instanceof Enumerated)
            return new EnumeratedValidator(annotation);
        if (annotation instanceof Min)
            return new MinValidator(annotation);
        if (annotation instanceof Max)
            return new MaxValidator(annotation);
        if (annotation instanceof Pattern)
            return new PatternValidator(annotation);
        else
            throw new RuntimeException("无法识别的标签类型" + annotation.getClass().getName());
    }

    public abstract String validate(Object value);
}
