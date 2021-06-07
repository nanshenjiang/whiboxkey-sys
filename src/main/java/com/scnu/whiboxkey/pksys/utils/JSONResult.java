package com.scnu.whiboxkey.pksys.utils;

public class JSONResult {
    private Integer code;
    private Object data;
    private String msg;

    public static JSONResult ok(Object data) {
        return new JSONResult(200,data,"success");
    }

    public static JSONResult error(Integer code,String msg) {
        return new JSONResult(code,null,msg);
    }

    public JSONResult() {
        super();
    }

    public JSONResult(Integer code, Object data, String msg) {
        super();
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
