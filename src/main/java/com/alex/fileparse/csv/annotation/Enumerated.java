package com.alex.fileparse.csv.annotation;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-26
 * Time: 下午7:07
 * To change this template use File | Settings | File Templates.
 */
public @interface Enumerated {
    EnumType value() default EnumType.ORDINAL;
}
