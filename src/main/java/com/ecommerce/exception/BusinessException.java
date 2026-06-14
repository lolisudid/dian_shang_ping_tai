package com.ecommerce.exception;

import lombok.Getter;

/**
 * 业务异常，由全局处理器转为 JSON Result。
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        this(400, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
