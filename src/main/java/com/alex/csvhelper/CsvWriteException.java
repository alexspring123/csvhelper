package com.alex.csvhelper;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-26
 * Time: 下午6:28
 * To change this template use File | Settings | File Templates.
 */
public class CsvWriteException extends Exception {
    public CsvWriteException() {
    }

    public CsvWriteException(String message) {
        super(message);
    }

    public CsvWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
