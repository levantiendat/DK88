package com.example.dk88;

import java.io.InputStream;

public class CustomResponseObject extends ResponseObject{
    public CustomResponseObject() {
        super();
    }

    public CustomResponseObject(int respCode, String message, Object data) {
        super(respCode, message, data);
    }

    // Các getter và setter của ResponseObject nếu cần

    // Định nghĩa lại phương thức byteStream()
    public InputStream byteStream() {
        if (getData() == null) {
            return null;
        }
        if (getData() instanceof InputStream) {
            return (InputStream) getData();
        }
        return null;
    }
}
