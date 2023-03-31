package com.example.dk88;

public class ResponseObject {
    private int respCode;
    private String message;
    private Object data;

    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_REQUEST_ERROR = 1;
    public static final int RESPONSE_SYSTEM_ERROR = 2;

    public ResponseObject() {
        super();
        respCode = RESPONSE_OK;
        message = "OK";
    }

    public ResponseObject(int respCode, String message, Object data) {
        super();
        this.respCode = respCode;
        this.message = message;
        this.data = data;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
