package com.hzdq.bajiesleepchildrenHD.dataclass;


public class UpLoadPicResponse  {


    private int code;
    private String message;
    private String data;



    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UpLoadPicResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
