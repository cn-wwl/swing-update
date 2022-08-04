package com.wwl.core.base;

import com.wwl.core.expection.IException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author wwl
 * @date 2022/7/27 14:07
 * @desc 结果
 */
public class Result<T> {

    public static final String SUCCESSFUL_CODE = "200";
    public static final String FAIL_CODE = "500";
    public static final String FAIL_MSG = "操作失败";

    private String code;

    private String error;

    private String timestamp;

    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = df.format(LocalDateTime.now());
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code 结果code
     * @param msg 结果消息
     * @param data 结果数据
     */
    private Result(String code, String msg, T data) {
        this.code = code;
        this.error = msg;
        this.data = data;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = df.format(LocalDateTime.now());
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data 成功返回数据
     * @return Result
     */
    public static <R> Result<R> success(R data) {
        return new Result<>(SUCCESSFUL_CODE, "", data);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static Result<Boolean> success() {
        return success(true);
    }

    /**
     * 操作失败
     * @return Result
     */
    public static <R>  Result<R> fail() {
        return new Result<>(FAIL_CODE, FAIL_MSG, null);
    }

    /**
     * 操作失败
     * @param code 错误类型
     * @param error 错误类型
     * @return Result
     */
    public static <R> Result<R> fail(String code,String error) {
        if(Objects.equals(code, SUCCESSFUL_CODE)){
            code = code+"0";
        }
        return new Result<>(code,error,null);
    }

    /**
     * 操作失败
     * @param error 错误类型
     * @return Result
     */
    public static <R> Result<R> fail(String error) {
        return new Result<>(FAIL_CODE,error,null);
    }
    /**
     * 操作失败
     * @param code 错误代码
     * @param error 错误类型
     * @param data 错误数据
     * @return Result
     */
    public static <R> Result<R> fail(String code,String error,R data) {
        return new Result<>(code,error,data);
    }


    /**
     * 操作出错
     * @param errorType 错误类型
     * @return 错误结果
     */
    public static <R> Result<R> fail(ErrorType errorType) {
        return fail(errorType.getCode(),errorType.getMessage());
    }

    /**
     * 根绝异常返回错误信息
     * @param exception 异常数据
     * @param <R> 返回类型
     * @return 错误结果
     */
    public static <R> Result<R> fail(IException exception)
    {
        return fail(exception.getExceptionCode()+"",exception.getExceptionMessage());
    }

    /**
     * 指定错误信息的 错误类型 返回结果
     * @param errorType 错误类型
     * @param error 错误消息
     * @return 错误结果
     */
    public static <R> Result<R> fail(ErrorType errorType,String error) {
        return fail(errorType.getCode(),error);
    }

    /**
     * 成功code=000000
     *
     * @return true/false
     */
    public boolean isSuccess() {
        return SUCCESSFUL_CODE.equals(this.code);
    }

    /**
     * 失败
     *
     * @return true/false
     */
    public boolean isFail() {
        return !isSuccess();
    }
}

