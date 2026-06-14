package com.ecommerce.common;

import java.io.Serializable;

public record Result<T>(int code, String message, T data) implements Serializable {

    private static final long serialVersionUID = 1L;

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> fail(String message) {
        return fail(400, message);
    }
}