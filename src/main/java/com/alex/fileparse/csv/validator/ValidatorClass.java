package com.alex.fileparse.csv.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-7-26
 * Time: 下午6:38
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatorClass {
    Class<? extends FieldValidator> value();
}
