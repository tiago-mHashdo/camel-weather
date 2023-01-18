package org.acme.camel.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDto{
    public Double latitude;
    public Double longitude;
    public Double generationtime_ms;
    public Integer utc_offset_seconds;
    public String timezone;
    public String timezone_abbreviation;
    public Double elevation;
    public HourlyUnitsDto hourly_units;
    public HourlyDto hourly;
}




