package com.alex.csvhelper.validator;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-9-30
 * Time: 下午12:40
 * To change this template use File | Settings | File Templates.
 */
public class ValidateException extends Exception {

  public ValidateException() {
  }

  public ValidateException(String message) {
    super(message);
  }

  public ValidateException(String message, Throwable cause) {
    super(message, cause);
  }
}
