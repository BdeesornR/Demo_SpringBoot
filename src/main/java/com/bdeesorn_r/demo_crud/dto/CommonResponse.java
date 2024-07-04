package com.bdeesorn_r.demo_crud.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse {
    private String status;
    private String message;
    private Object data;

    public CommonResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public CommonResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
