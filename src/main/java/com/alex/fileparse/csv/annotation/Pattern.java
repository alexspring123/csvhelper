package com.alex.fileparse.csv.annotation;

import com.alex.fileparse.csv.validator.PatternValidator;
import com.alex.fileparse.csv.validator.ValidatorClass;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-26
 * Time: 下午7:02
 * To change this template use File | Settings | File Templates.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
@ValidatorClass(value = PatternValidator.class)
public @interface Pattern {
    String value() default "";

    String description() default "";
}
