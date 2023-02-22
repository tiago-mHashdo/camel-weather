package org.acme.camel.beans;

import org.acme.camel.dtos.WeatherDto;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperty;

import static org.acme.camel.constants.GeneralConstants.ARRIVAL_INSTANT_PROPERTY;

public class WeatherBean {

    /**
     * Bean responsible for getting the temperature of the arrival instant.
     *
     * @param weatherDto the payload of the open-meteo API
     * @param arrivalInstant the instant of arrival previously calculated
     * @return the temperature at the arrival instant as a Double
     */
    public Double getTemperature(@Body WeatherDto weatherDto, @ExchangeProperty(ARRIVAL_INSTANT_PROPERTY) String arrivalInstant) {
        int arrivalInstantIndex = weatherDto.getHourly().getTime().lastIndexOf(arrivalInstant);

        return weatherDto.getHourly().getTemperature_2m().get(arrivalInstantIndex);
    }
}
