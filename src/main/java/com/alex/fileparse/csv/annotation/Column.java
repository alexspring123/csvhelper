package com.alex.fileparse.csv.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-26
 * Time: 下午6:32
 * To change this template use File | Settings | File Templates.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface Column {
    String name() default "";

    boolean nullable() default true;

    boolean needTab() default false;

    int length() default 255;

    int precision() default 19;

    int scale() default 4;
}
