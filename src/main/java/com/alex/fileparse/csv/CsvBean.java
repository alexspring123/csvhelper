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
    private ProcessResult result = ProcessResult.success;
    private String message;
    private String content;

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public ProcessResult getResult() {
        return result;
    }

    public void setResult(ProcessResult result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addMessage(String message) {
        if (message == null || "".equals(message.trim()))
            return;
        StringBuilder sb = new StringBuilder();
        if (this.message != null)
            sb.append(this.message);
        sb.append(message.trim());
        this.message = sb.toString();
    }

    public boolean isSuccess() {
        return ProcessResult.success.equals(getResult());
    }

    public boolean isFailed() {
        return ProcessResult.failed.equals(getResult());
    }

    public boolean isIgnore() {
        return ProcessResult.ignore.equals(getResult());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CsvBean csvBean = (CsvBean) o;

        return lineNumber == csvBean.lineNumber;

    }

    @Override
    public int hashCode() {
        return lineNumber;
    }
}
