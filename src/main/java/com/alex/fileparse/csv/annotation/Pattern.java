package com.alex.fileparse.csv.annotation;

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
@Target(value = {ElementType.METHOD, ElementType.FIELD})
public @interface Pattern {
    String value() default "";

    String description() default "";
}