package com.reopenai.bookstore.component.i18n;

import com.reopenai.bookstore.bean.ErrorCode;
import com.reopenai.bookstore.component.constants.EmptyConstants;
import com.reopenai.bookstore.component.constants.JavaTimeConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * internationalized utils
 *
 * @author Allen Huang
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class I18nUtil {

    private static final NumberFormat NUMBER_FORMAT;

    static {
        NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.CHINA);
        NUMBER_FORMAT.setMaximumIntegerDigits(32);
        NUMBER_FORMAT.setMaximumFractionDigits(32);
        NUMBER_FORMAT.setRoundingMode(RoundingMode.CEILING);
    }

    static MessageSource messageSource;

    /**
     * Parse the localized internationalized message corresponding to the error code using the provided format parameters.
     * Error codes are globally unique, and each error code maps to an internationalization code.
     *
     * @param errorCode error code
     * @return The resolved internationalized message
     * @see ErrorCode
     */
    public static String parseLocaleMessage(ErrorCode errorCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return parseLocaleMessage(locale, errorCode.getCode());
    }


    /**
     * Parse the localized internationalized message corresponding to the error code using the provided format parameters.
     * Error codes are globally unique, and each error code maps to an internationalization code.
     *
     * @param errorCode error code
     * @return The resolved internationalized message
     * @see ErrorCode
     */
    public static String parseLocaleMessage(Locale locale, ErrorCode errorCode) {
        return parseLocaleMessage(locale, errorCode.getCode());
    }

    /**
     * Resolve the localized internationalized message corresponding to the i18n code
     *
     * @param code error code
     * @return The resolved internationalized message
     */
    public static String parseLocaleMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return parseLocaleMessage(locale, code, EmptyConstants.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Resolve the localized internationalized message corresponding to the i18n code
     *
     * @param code error code
     * @return The resolved internationalized message
     */
    public static String parseLocaleMessage(Locale locale, String code) {
        return parseLocaleMessage(locale, code, EmptyConstants.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Parse the localized internationalized message corresponding to the error code using the provided format parameters.
     * Error codes are globally unique, and each error code maps to an internationalization code.
     *
     * @param errorCode error code
     * @param args      i18n params
     * @return The resolved internationalized message
     * @see ErrorCode
     */
    public static String parseLocaleMessage(ErrorCode errorCode, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return parseLocaleMessage(locale, errorCode.getCode(), args);
    }

    /**
     * Parse the localized internationalized message corresponding to the error code using the provided format parameters.
     * Error codes are globally unique, and each error code maps to an internationalization code.
     *
     * @param errorCode error code
     * @param args      i18n params
     * @return The resolved internationalized message
     * @see ErrorCode
     */
    public static String parseLocaleMessage(Locale locale, ErrorCode errorCode, Object... args) {
        return parseLocaleMessage(locale, errorCode.getCode(), args);
    }

    /**
     * Resolve the localized internationalized message for the specified i18n code using the provided format parameters
     *
     * @param code error code
     * @param args i18n params
     * @return The resolved internationalized message
     */
    public static String parseLocaleMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return parseLocaleMessage(locale, code, args);
    }

    /**
     * Resolve the localized internationalized message for the specified i18n code using the provided format parameters
     *
     * @param code error code
     * @param args i18n params
     * @return The resolved internationalized message
     */
    public static String parseLocaleMessage(Locale locale, String code, Object... args) {
        try {
            Object[] vars = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                // 将BigDecimal转换成toPlainString()
                if (arg instanceof BigDecimal numberValue) {
                    arg = numberValue.stripTrailingZeros().toPlainString();
                } else if (arg instanceof LocalDateTime time) {
                    arg = time.format(JavaTimeConstants.DATE_TIME_FORMATTER);
                } else if (arg instanceof Number number) {
                    arg = NUMBER_FORMAT.format(number);
                }
                vars[i] = arg;
            }
            return messageSource.getMessage(code, vars, locale);
        } catch (Exception e) {
            return code;
        }
    }

}
