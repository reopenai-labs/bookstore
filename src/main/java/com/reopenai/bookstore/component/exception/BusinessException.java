package com.reopenai.bookstore.component.exception;

import com.reopenai.bookstore.bean.ErrorCode;
import com.reopenai.bookstore.component.i18n.I18nUtil;
import lombok.Getter;

import java.util.Locale;

/**
 * A base exception class - all custom exceptions should extend this.
 *
 * @author Allen Huang
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * error code
     */
    private final ErrorCode errorCode;
    /**
     * Invalid parameter. This parameter can be used to construct internationalized exception messages.
     */
    private final Object[] args;

    public BusinessException(ErrorCode errorCode, Object... args) {
        this(Locale.SIMPLIFIED_CHINESE, errorCode, args);
    }

    public BusinessException(Locale locale, ErrorCode errorCode, Object... args) {
        super(I18nUtil.parseLocaleMessage(locale, errorCode, args));
        this.errorCode = errorCode;
        this.args = args;
    }

    public BusinessException(Throwable cause, ErrorCode errorCode, Object... args) {
        this(Locale.SIMPLIFIED_CHINESE, cause, errorCode, args);
    }

    public BusinessException(Locale locale, Throwable cause, ErrorCode errorCode, Object... args) {
        super(I18nUtil.parseLocaleMessage(locale, errorCode, args), cause);
        this.errorCode = errorCode;
        this.args = args;
    }

}
