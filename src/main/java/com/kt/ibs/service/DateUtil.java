package com.kt.ibs.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DateUtil {

    private DateUtil() {

    }
public final static Date START_OF_DAY = new Date(System.currentTimeMillis() -  (System.currentTimeMillis() - LocalDate.now().toEpochDay()));

    private static final DateFormat T24_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
    private static final DateFormat T24_DATE_TIME_FORMAT = new SimpleDateFormat("yyMMddHHmm");
    private static final int SECONDS_FOR_SYNC = 60 * 10;

    public static Date fromT24Date(final String t24Date) {
        try {
            return T24_DATE_FORMAT.parse(t24Date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    
    public void test() {
    Long sinceStartOfDay =   (System.currentTimeMillis() - LocalDate.now().toEpochDay());
    }

    public static boolean shouldRefreshWithCore(final Date lastSyncTime) {
        return true;
        // return (lastSyncTime == null) || ((System.currentTimeMillis() - lastSyncTime.getTime()) > (SECONDS_FOR_SYNC *
        // 1000));
    }

    public static Date fromT24DateTime(final String t24Date) {
        try {
            return T24_DATE_TIME_FORMAT.parse(t24Date);
        } catch (ParseException e) {
            throw new RuntimeException("Error with T24 Date time", e);
        }
    }
}
