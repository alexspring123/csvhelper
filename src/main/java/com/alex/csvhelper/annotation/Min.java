package com.alex.csvhelper.annotation;

import com.alex.csvhelper.validator.MinValidator;
import com.alex.csvhelper.validator.ValidatorClass;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-26
 * Time: 下午6:59
 * To change this template use File | Settings | File Templates.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
@ValidatorClass(value = MinValidator.class)
public @interface Min {
    double value() default 0;

    boolean allowEqual() default true;
}
