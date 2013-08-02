package com.alex.fileparse.csv.validator;

import com.alex.fileparse.csv.annotation.Enumeration;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-7-26
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
public class EnumerationValidator implements Validator<Enumeration> {

    @Override
    public String validate(Enumeration annotation, Object value) {
        if (annotation == null || value == null)
            return null;

        if (value instanceof String) {
            String[] enumValues = annotation.values();
            if (Arrays.asList(enumValues).contains(value) == false)
                return "只能填写 " + StringUtils.join(enumValues, ',');
        }

        return null;
    }
}
