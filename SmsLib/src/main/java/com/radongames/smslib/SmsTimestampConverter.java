package com.radongames.smslib;

import com.radongames.core.interfaces.Converter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.NoArgsConstructor;

/**
 * Note: This class assumes that if the timestamp has 13 or more digits, it is in epoch millis.
 * Else it is in epoch seconds.
 * <p>
 * On the reverse side, it always returns epoch seconds.
 */
@Singleton
@NoArgsConstructor(onConstructor_ = {@Inject})
public class SmsTimestampConverter implements Converter<Long, String> {

    private static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    /**
     * Convert a timestamp to a String. If the timstamp has 13 or more digits, we assume it counts milliseconds
     * since 1/1/1970 (Unix epoch). This is true for {@code System.currentTimeMillis()}
     *
     * @param ts The timestamp to convert
     * @return Its string representation
     */
    @Override
    public String forward(Long ts) {

        return ZonedDateTime.ofInstant(isMillis(ts) ? Instant.ofEpochMilli(ts) : Instant.ofEpochSecond(ts), ZoneId.systemDefault()).format(DEFAULT_DATE_FORMAT);
    }

    /**
     * Parse a String and convert it to a timestamp. This always returns epoch millis.
     * @param s     The String to parse
     * @return      Its milli representation
     */
    @Override
    public Long backward(String s) {

        return ZonedDateTime.parse(s, DEFAULT_DATE_FORMAT).toInstant().toEpochMilli();
    }

    private boolean isMillis(long ts) {

        return ts >= 1000000000000L;
    }
}
