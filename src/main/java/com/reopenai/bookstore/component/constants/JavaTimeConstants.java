package com.reopenai.bookstore.component.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Java Time Utility Class
 *
 * @author Allen Huang
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class JavaTimeConstants {

    /**
     * System default TimeZone
     */
    public static final ZoneOffset SYSTEM_ZONE_OFFSET = OffsetDateTime.now().getOffset();

    /**
     * Time format pattern
     */
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Date format pattern
     */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Simplified date format pattern
     */
    public static final DateTimeFormatter SIMPLE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Date and time format pattern
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Simplified hour-precise time format pattern
     */
    public static final DateTimeFormatter SIMPLE_HOUR_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHH");

    /**
     * Simplified minute-precise time format pattern
     */
    public static final DateTimeFormatter SIMPLE_MINUTES_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    /**
     * Simplified date-time format pattern
     */
    public static final DateTimeFormatter SIMPLE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Precise date-time format pattern (with milliseconds)
     */
    public static final DateTimeFormatter PRECISE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    /**
     * Simplified precise date-time format pattern (with milliseconds)
     */
    public static final DateTimeFormatter SIMPLE_PRECISE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

}
