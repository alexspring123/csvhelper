package com.alex.fileparse.csv.validator;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-8-1
 * Time: 下午4:23
 * To change this template use File | Settings | File Templates.
 */
public class BooleanValidator implements Validator<com.alex.fileparse.csv.annotation.Boolean> {
    @Override
    public String validate(com.alex.fileparse.csv.annotation.Boolean annotation, Object value) {
        if (annotation == null || value == null)
            return null;

        return null;
    }
}
