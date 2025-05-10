package com.reopenai.bookstore.bean;

import com.reopenai.bookstore.component.i18n.I18nUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Locale;

/**
 * Created by Allen Huang
 */
@Data
public class ApiResponse<T> {

    @Schema(description = "code.200 means success")
    private String code;

    @Schema(description = "Description of the response")
    private String message;

    @Schema(description = "The data returned when the request is successful")
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> entity = new ApiResponse<>();
        entity.setData(data);
        entity.setCode(ErrorCode.SUCCESS.getCode());
        entity.setMessage("success");
        return entity;
    }

    public static <T> ApiResponse<T> failure(Locale locale, ErrorCode code, Object... args) {
        ApiResponse<T> result = new ApiResponse<>();
        result.setCode(code.getCode());
        result.setMessage(I18nUtil.parseLocaleMessage(locale, code, args));
        return result;
    }

    public static <T> ApiResponse<T> failureWithMessage(ErrorCode code, String message) {
        ApiResponse<T> result = new ApiResponse<>();
        result.setMessage(message);
        result.setCode(code.getCode());
        return result;
    }

}
