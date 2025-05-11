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
     * Date format pattern
     */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * Date and time format pattern
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
