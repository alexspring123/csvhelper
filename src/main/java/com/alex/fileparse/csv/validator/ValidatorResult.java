package com.alex.fileparse.csv.validator;

/**
 * 验证器返回结果
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-7-26
 * Time: 下午5:13
 * To change this template use File | Settings | File Templates.
 */
public class ValidatorResult {
    private Object value = null;
    private boolean isvalid = true;
    private String errorMsg = null;

    public void addErrorMgr(String msg) {
        if (msg == null)
            return;
        StringBuilder sb = new StringBuilder();
        if (errorMsg != null)
            sb.append(errorMsg);
        sb.append(msg);
        this.errorMsg = sb.toString();

        if (this.isIsvalid()) {
            setIsvalid(false);
        }
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isIsvalid() {
        return isvalid;
    }

    public void setIsvalid(boolean isvalid) {
        this.isvalid = isvalid;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
