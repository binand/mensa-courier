package com.radongames.smslib;

import com.radongames.core.interfaces.Converter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.NoArgsConstructor;

@Singleton
@NoArgsConstructor(onConstructor_={@Inject})
public class SmsTimestampConverter implements Converter<Long, String> {

    private static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    @Override
    public String forward(Long ts) {

        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault()).format(DEFAULT_DATE_FORMAT);
    }

    @Override
    public Long backward(String s) {

        return ZonedDateTime.parse(s, DEFAULT_DATE_FORMAT).toInstant().toEpochMilli();
    }
}
