package org.acme.camel.routes;

import org.acme.camel.beans.InstantBean;
import org.acme.camel.beans.WeatherBean;
import org.acme.camel.constants.GeneralConstants;
import org.acme.camel.dtos.WeatherDto;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;

import static org.acme.camel.constants.GeneralConstants.*;
import static org.acme.camel.utils.AppUtils.direct;
import static org.apache.camel.support.builder.PredicateBuilder.and;

public class StandardWeatherRoute extends RouteBuilder {

    @Override
    public void configure() {
        onException(Exception.class)
                .handled(true)
                .setBody(simple("Error: ${exception.message}"))
                .to("log:error");

        mainRoute();
        getTravelDurationAndCoordinates();
        getWeatherForecast();
        validateLocationsEndpoint();
    }

    /**
     * Defines the main Camel route for retrieving weather information, which accepts a GET request
     * at the endpoint /weather. This route validates the origin and destination locations, retrieves
     * the travel duration and coordinates using the Google Maps API, retrieves the weather forecast
     * at the destination using the Open-Meteo API, and constructs a message with the arrival time and
     * temperature to return to the user.
     */
    private void mainRoute() {
        from("jetty:{{weather.server.host}}:{{weather.server.port}}/weather?httpMethodRestrict=GET")
                .routeId(MAIN_ROUTE)
                .log(LoggingLevel.DEBUG, STARTED_ROUTE_LOG + MAIN_ROUTE)
                .to(direct(VALIDATE_LOCATIONS_ENDPOINT))
                .log(LoggingLevel.DEBUG, "|- Locations validated")
                .to(direct(GET_TRAVEL_DURATION_AND_COORDINATES_ENDPOINT))
                .log(LoggingLevel.DEBUG, "|- Travel Duration: " +
                        TRAVEL_DURATION_EXCHANGE_PROPERTY + " seconds")
                .setProperty(ARRIVAL_INSTANT_PROPERTY, method(InstantBean.class))
                .to(direct(GET_WEATHER_FORECAST_ENDPOINT))
                .setProperty(EXCHANGE_MESSAGE_PROPERTY, simple("By going with the fastest google maps route you " +
                        "will arrive at: " + ARRIVAL_INSTANT_EXCHANGE_PROPERTY
                        + " and when you do arrive the temperature will be: " + TEMPERATURE_EXCHANGE_PROPERTY + "??"))
                .setBody(simple(EXCHANGE_MESSAGE_EXCHANGE_PROPERTY))
                .log(LoggingLevel.DEBUG, ENDED_ROUTE_LOG + MAIN_ROUTE);
    }

    /**
     * Route that validates that the googleMaps.apiKey is not null, nor empty, nor the default value and afterwards
     * retrieves the travel duration {@link GeneralConstants#TRAVEL_DURATION_PROPERTY}and
     * destination latitude {@link GeneralConstants#DESTINATION_LATITUDE_PROPERTY} and
     * destination longitude {@link GeneralConstants#DESTINATION_LONGITUDE_PROPERTY} from the
     * Google Maps API.
     */
    private void getTravelDurationAndCoordinates() {
        from(direct(GET_TRAVEL_DURATION_AND_COORDINATES_ENDPOINT))
                .routeId(GET_TRAVEL_DURATION_AND_COORDINATES_ROUTE)
                .log(LoggingLevel.DEBUG, STARTED_ROUTE_LOG + GET_TRAVEL_DURATION_AND_COORDINATES_ROUTE)
                .validate(and(simple("{{googleMaps.apiKey}}").isNotNull(),
                        simple("{{googleMaps.apiKey}}").isNotEqualTo(""),
                        simple("{{googleMaps.apiKey}}").isNotEqualTo("myKey")))
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .setHeader(Exchange.HTTP_PATH, constant("/maps/api/directions/json"))
                .setHeader(Exchange.HTTP_QUERY, simple("origin=" + ORIGIN_EXCHANGE_PROPERTY +
                        "&destination=" + DESTINATION_EXCHANGE_PROPERTY + "&key={{googleMaps.apiKey}}"))
                .to("https://maps.googleapis.com" + BRIDGE_ENDPOINT_TRUE_CONST)
                .setProperty(TRAVEL_DURATION_PROPERTY, jsonpath("$.routes.[0].legs[0].duration.value"))
                .setProperty(DESTINATION_LATITUDE_PROPERTY, jsonpath("$.routes.[0].legs[0].end_location.lat"))
                .setProperty(DESTINATION_LONGITUDE_PROPERTY, jsonpath("$.routes.[0].legs[0].end_location.lng"))
                .log(LoggingLevel.DEBUG, "|- Arrival Instant: " + ARRIVAL_INSTANT_EXCHANGE_PROPERTY)
                .log(LoggingLevel.DEBUG, "|- Temperature: " + TEMPERATURE_EXCHANGE_PROPERTY)
                .removeHeaders(CAMEL_HTTP_HEADER_PATTERN)
                .log(LoggingLevel.DEBUG, ENDED_ROUTE_LOG + GET_TRAVEL_DURATION_AND_COORDINATES_ROUTE);
    }

    /**
     * Route that retrieves the weather forecast from Open Meteo API for the destination coordinates
     * stored in the {@link GeneralConstants#DESTINATION_LATITUDE_PROPERTY} and
     * {@link GeneralConstants#DESTINATION_LONGITUDE_PROPERTY} exchange properties.
     * The retrieved temperature value in Celsius degrees is stored in the
     * {@link GeneralConstants#TEMPERATURE_PROPERTY} exchange property.
     */
    private void getWeatherForecast() {
        from(direct(GET_WEATHER_FORECAST_ENDPOINT))
                .routeId(GET_WEATHER_FORECAST_ROUTE)
                .log(LoggingLevel.DEBUG, STARTED_ROUTE_LOG + GET_WEATHER_FORECAST_ROUTE)
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .setHeader(Exchange.HTTP_PATH, constant("/v1/forecast"))
                .setHeader(Exchange.HTTP_QUERY, simple("latitude=" + DESTINATION_LATITUDE_EXCHANGE_PROPERTY +
                        "&longitude=" + DESTINATION_LONGITUDE_EXCHANGE_PROPERTY +
                        "&hourly=temperature_2m,precipitation&timezone=auto"))
                .to("https://api.open-meteo.com" + BRIDGE_ENDPOINT_TRUE_CONST)
                .unmarshal().json(JsonLibrary.Jackson, WeatherDto.class)
                .setProperty(TEMPERATURE_PROPERTY, method(WeatherBean.class))
                .removeHeaders(CAMEL_HTTP_HEADER_PATTERN)
                .log(LoggingLevel.DEBUG, ENDED_ROUTE_LOG + GET_WEATHER_FORECAST_ROUTE);
    }

    /**
     * Validates the origin and destination locations by checking if they meet the required format
     * before storing them as exchange properties.
     */
    private void validateLocationsEndpoint() {
        from(direct(VALIDATE_LOCATIONS_ENDPOINT))
                .routeId(VALIDATE_LOCATIONS_ROUTE_ID)
                .log(LoggingLevel.DEBUG, STARTED_ROUTE_LOG + VALIDATE_LOCATIONS_ROUTE_ID)
                .validate(header(ORIGIN_CONST).regex("^([a-zA-Z0-9\\-\\s\\.',]+)$"))
                .validate(header(DESTINATION_CONST).regex("^([a-zA-Z0-9\\-\\s\\.',]+)$"))
                .setProperty(ORIGIN_PROPERTY, header(ORIGIN_CONST))
                .setProperty(DESTINATION_PROPERTY, header(DESTINATION_CONST))
                .log(LoggingLevel.DEBUG, ENDED_ROUTE_LOG + VALIDATE_LOCATIONS_ROUTE_ID);
    }
}
