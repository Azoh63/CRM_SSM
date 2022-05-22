package cn.edu.yibinu.crm.commons.domain;

/**
 * commons包一般放公共类
 */
public class ReturnObject {
    private String code;    //1是登录成功，0是失败
    private String message;     //失败的信息
    private Object retData; //其他的响应信息

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
