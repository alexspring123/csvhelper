package com.alex.csvhelper.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alex.csvhelper.validator.BooleanValidator;
import com.alex.csvhelper.validator.ValidatorClass;

/**
 * Created with IntelliJ IDEA. User: alex Date: 13-5-26 Time: 下午6:53 To change
 * this template use File | Settings | File Templates.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
@ValidatorClass(value = BooleanValidator.class)
public @interface Boolean {
	String trueStr() default "是";

	String falseStr() default "否";
}
