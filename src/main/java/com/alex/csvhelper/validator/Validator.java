package com.alex.csvhelper.validator;

import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-7-27
 * Time: 上午9:53
 * To change this template use File | Settings | File Templates.
 */
public interface Validator<A extends Annotation> {
  public void validate(A annotation, Object content) throws ValidateException;
}
