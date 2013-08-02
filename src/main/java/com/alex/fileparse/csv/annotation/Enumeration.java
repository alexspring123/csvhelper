package com.alex.fileparse.csv.annotation;

import com.alex.fileparse.csv.validator.EnumerationValidator;
import com.alex.fileparse.csv.validator.ValidatorClass;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-7-26
 * Time: 下午4:30
 * To change this template use File | Settings | File Templates.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
@ValidatorClass(value = EnumerationValidator.class)
public @interface Enumeration {
    String[] values() default {};
}
