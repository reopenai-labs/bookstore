package com.reopenai.bookstore.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Allen Huang
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * success
     */
    SUCCESS("200"),
    /**
     * 资源不存在
     */
    NOT_FOUND("404"),
    /**
     * 请求方法错误
     */
    METHOD_NOT_ALLOWED("405"),
    /**
     * 无法解析ACCEPT
     */
    NOT_ACCEPTABLE("406"),
    /**
     * 请求的Media type错误
     */
    MEDIA_TYPE_NOT_ALLOWED("415"),
    /**
     * 请求太多被熔断
     */
    MANY_REQUEST("429"),
    /**
     * 未知异常
     */
    SERVER_ERROR("500"),
    /**
     * 未通过参数检查
     */
    FAILED_PARAMETER_CHECK("4001"),
    /**
     * 请求参数丢失
     */
    MISSING_REQUEST_PARAMETER("4002"),
    /**
     * 非法的参数值
     */
    INVALID_PARAMETER("4003"),
    /**
     * 请求参数类型不匹配
     */
    PARAM_TYPE_MISMATCH("4004"),

    BOOK_CATEGORY_EXISTS("101001"),

    DATA_NOT_FOUND("101002"),

    ;

    private final String code;

}
