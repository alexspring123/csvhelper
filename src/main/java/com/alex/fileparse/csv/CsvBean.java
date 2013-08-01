package com.alex.fileparse.csv;

import java.io.Serializable;

/**
 * CSV行对象类
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-26
 * Time: 下午6:11
 */
public class CsvBean implements Serializable {
    private int lineNumber;
    private boolean valid = true;
    private String errorMsg;

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void addErrorMsg(String errorMsg) {
        if (errorMsg == null)
            return;
        StringBuilder sb = new StringBuilder();
        if (this.errorMsg != null)
            sb.append(this.errorMsg);
        sb.append(errorMsg);
        this.errorMsg = sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CsvBean csvBean = (CsvBean) o;

        if (lineNumber != csvBean.lineNumber) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return lineNumber;
    }
}
