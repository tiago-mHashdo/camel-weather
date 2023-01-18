package org.acme.camel.beans;

import org.apache.camel.ExchangeProperty;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.acme.camel.routes.StandardWeatherRoute.TRAVEL_DURATION_PROPERTY;


public class InstantBean {

    public static final String HOURLY_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";

    public String formatInstant(@ExchangeProperty(TRAVEL_DURATION_PROPERTY) String travelDuration) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(HOURLY_TIME_FORMAT).withZone(ZoneId.systemDefault());

        Instant instant = Instant.now();
        Duration travelSeconds = Duration.ofSeconds(Long.parseLong(travelDuration));
        Instant arrivalInstant = instant.plus(travelSeconds);
        int arrivalMinute = instant.atZone(ZoneOffset.UTC).getMinute();
        arrivalInstant = arrivalMinute < 30 ? arrivalInstant.truncatedTo(ChronoUnit.HOURS) :
                arrivalInstant.truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.HOURS);

        return formatter.format(arrivalInstant);
    }

}
