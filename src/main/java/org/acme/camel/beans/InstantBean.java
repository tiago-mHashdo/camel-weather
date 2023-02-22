package org.acme.camel.beans;

import org.apache.camel.ExchangeProperty;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.acme.camel.constants.GeneralConstants.TRAVEL_DURATION_PROPERTY;

public class InstantBean {

    public static final String HOURLY_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";

    /**
     * Bean responsible for adding the travel duration to this instant, rounding it up if the minutes are equal or bigger
     * than 30, otherwise rounding it down and then formatting it with the same format of the open-meteo API.
     *
     * @param travelDuration {@link org.acme.camel.constants.GeneralConstants#TRAVEL_DURATION_PROPERTY}
     * @return the formatted time as a String
     */
    public String formatInstant(@ExchangeProperty(TRAVEL_DURATION_PROPERTY) String travelDuration) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(HOURLY_TIME_FORMAT).withZone(ZoneId.systemDefault());

        Instant instant = Instant.now();
        Duration travelSeconds = Duration.ofSeconds(Long.parseLong(travelDuration));
        Instant arrivalInstant = instant.plus(travelSeconds);
        int arrivalMinute = instant.atZone(ZoneOffset.UTC).getMinute();
        // If the arrival minute is smaller than 30, then round the instant down, otherwise round it up
        arrivalInstant = arrivalMinute < 30 ? arrivalInstant.truncatedTo(ChronoUnit.HOURS) :
                arrivalInstant.truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.HOURS);

        return formatter.format(arrivalInstant);
    }

}
