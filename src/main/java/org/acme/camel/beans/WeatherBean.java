package org.acme.camel.beans;

import org.acme.camel.dtos.WeatherDto;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperty;

import static org.acme.camel.routes.StandardWeatherRoute.ARRIVAL_INSTANT_PROPERTY;

public class WeatherBean {

    public Double getTemperature(@Body WeatherDto weatherDto, @ExchangeProperty(ARRIVAL_INSTANT_PROPERTY) String arrivalInstant) {
        int arrivalInstantIndex = weatherDto.getHourly().getTime().lastIndexOf(arrivalInstant);

        return weatherDto.getHourly().getTemperature_2m().get(arrivalInstantIndex);
    }
}
