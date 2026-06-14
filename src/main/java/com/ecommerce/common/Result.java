package com.ecommerce.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一 API 返回封装，所有 Controller 必须返回此类型。
 *
 * @param <T> data 字段的业务数据类型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 业务状态码，200 表示成功 */
    private int code;
    /** 提示信息 */
    private String message;
    /** 实际数据，可为 null */
    private T data;

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> ok(String message, T data) {
        Result<T> r = ok(data);
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> fail(int code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> fail(String message) {
        return fail(400, message);
    }
}
