package org.acme.camel.routes;

import org.acme.camel.beans.InstantBean;
import org.acme.camel.beans.WeatherBean;
import org.acme.camel.dtos.WeatherDto;
import org.acme.camel.utils.SimpleUtils;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;

public class StandardWeatherRoute extends RouteBuilder {
    // Properties
    public static final String ORIGIN_PROPERTY = "originProperty";
    public static final String DESTINATION_PROPERTY = "destinationProperty";
    public static final String TRAVEL_DURATION_PROPERTY = "travelDurationProperty";
    public static final String ARRIVAL_INSTANT_PROPERTY = "arrivalInstantProperty";
    public static final String DESTINATION_LATITUDE_PROPERTY = "destinationLatitudeProperty";
    public static final String DESTINATION_LONGITUDE_PROPERTY = "destinationLongitudeProperty";
    public static final String TEMPERATURE_PROPERTY = "temperatureProperty";


    // Exchange properties
    public static final String ORIGIN_EXCHANGE_PROPERTY = SimpleUtils.getExchangeProperty(ORIGIN_PROPERTY);
    public static final String DESTINATION_EXCHANGE_PROPERTY = SimpleUtils.getExchangeProperty(DESTINATION_PROPERTY);
    public static final String TRAVEL_DURATION_EXCHANGE_PROPERTY = SimpleUtils.getExchangeProperty(TRAVEL_DURATION_PROPERTY);
    public static final String ARRIVAL_INSTANT_EXCHANGE_PROPERTY = SimpleUtils.getExchangeProperty(ARRIVAL_INSTANT_PROPERTY);
    public static final String DESTINATION_LATITUDE_EXCHANGE_PROPERTY = SimpleUtils.getExchangeProperty(DESTINATION_LATITUDE_PROPERTY);
    public static final String DESTINATION_LONGITUDE_EXCHANGE_PROPERTY = SimpleUtils.getExchangeProperty(DESTINATION_LONGITUDE_PROPERTY);
    public static final String TEMPERATURE_EXCHANGE_PROPERTY = SimpleUtils.getExchangeProperty(TEMPERATURE_PROPERTY);

    // Constants
    public static final String ORIGIN_CONST = "origin";
    public static final String DESTINATION_CONST = "destination";
    public static final String BRIDGE_ENDPOINT_TRUE_CONST = "&bridgeEndpoint=true";


    @Override
    public void configure() throws Exception {
        from("jetty:http://localhost:8080/weather?httpMethodRestrict=GET")
                .setProperty(ORIGIN_PROPERTY, header(ORIGIN_CONST))
                .setProperty(DESTINATION_PROPERTY, header(DESTINATION_CONST))
                .log("|- Received Origin: " + ORIGIN_EXCHANGE_PROPERTY)
                .log("|- Received Destination: " + DESTINATION_EXCHANGE_PROPERTY)
                .toD("https://maps.googleapis.com/maps/api/directions/json?origin=" + ORIGIN_EXCHANGE_PROPERTY +
                        "&destination=" + DESTINATION_EXCHANGE_PROPERTY + BRIDGE_ENDPOINT_TRUE_CONST + "&key={{googleMaps.apiKey}}")
                .setProperty(TRAVEL_DURATION_PROPERTY, jsonpath("$.routes.[0].legs[0].duration.value"))
                .setProperty(DESTINATION_LATITUDE_PROPERTY, jsonpath("$.routes.[0].legs[0].end_location.lat"))
                .setProperty(DESTINATION_LONGITUDE_PROPERTY, jsonpath("$.routes.[0].legs[0].end_location.lng"))
                .log("|- Travel Duration: " + TRAVEL_DURATION_EXCHANGE_PROPERTY + " seconds")
                .setProperty(ARRIVAL_INSTANT_PROPERTY, method(InstantBean.class))
                .log("|- Arrival Instant: " + ARRIVAL_INSTANT_EXCHANGE_PROPERTY)
                .log("|- Destination Latitude: " + DESTINATION_LATITUDE_EXCHANGE_PROPERTY)
                .log("|- Destination Longitude: " + DESTINATION_LONGITUDE_EXCHANGE_PROPERTY)
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .setHeader(Exchange.HTTP_URI, constant("https://api.open-meteo.com/v1/forecast"))
                .setHeader(Exchange.HTTP_QUERY, simple("latitude=" + DESTINATION_LATITUDE_EXCHANGE_PROPERTY +
                        "&longitude=" + DESTINATION_LONGITUDE_EXCHANGE_PROPERTY +
                        "&hourly=temperature_2m,precipitation" + BRIDGE_ENDPOINT_TRUE_CONST + "&timezone=auto"))
                //  .setHeader(Exchange.HTTP_QUERY, constant("hourly=precipitation"))
                .process(exchange ->
                        System.out.println())
                .toD("https://api.open-meteo.com")
                .unmarshal().json(JsonLibrary.Jackson, WeatherDto.class)
                .setProperty(TEMPERATURE_PROPERTY, method(WeatherBean.class))
                .log("|- Temperature: " + TEMPERATURE_EXCHANGE_PROPERTY);
        //.log("Received response: ${body}");
    }
}
