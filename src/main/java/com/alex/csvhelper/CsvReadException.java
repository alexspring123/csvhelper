package com.alex.csvhelper;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-26
 * Time: 下午6:28
 * To change this template use File | Settings | File Templates.
 */
public class CsvReadException extends Exception {
    public CsvReadException() {
    }

    public CsvReadException(String message) {
        super(message);
    }

    public CsvReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
