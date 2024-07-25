package com.plog.server.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private String message;
    private Object data;

    public ApiResponse(String message) {
        this.message = message;
    }

    //데이터를 포함하는 경우
    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
