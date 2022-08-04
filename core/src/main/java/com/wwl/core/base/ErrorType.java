package com.wwl.core.base;

/**
 * @author wwl
 * @date 2022/7/27 14:09
 * @desc 系统异常
 */
public enum ErrorType {
    FRAME_ERROR("1001","框架底层异常"),

    COMPONENTS_ERROR("1002","系统组件异常"),

    MIC_SERVICES_ERROR("1003","微服务异常"),

    PARAMETER_ERROR("2001","参数错误"),

    TIMEOUT_ERROR("2002","响应超时"),

    SERVICES_BUSY("2003","服务繁忙"),

    SYSTEM_ERROR("501", "系统异常"),

    BUSINESS_ERROR("500", "操作异常"),

    NO_PERMISSION("401", "未授权"),

    FORBIDDEN("403", "禁止访问"),


    NOT_FOUND("404", "服务未找到"),

    GATEWAY_ERROR("502", "网关异常"),

    GATEWAY_CONNECT_TIME_OUT("503", "网关超时");
    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String message;

    ErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }



    public String getMessage() {
        return message;
    }
}
