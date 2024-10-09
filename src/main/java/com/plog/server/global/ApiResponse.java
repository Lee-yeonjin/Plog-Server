package com.plog.server.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String message;
    private T data;

    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
