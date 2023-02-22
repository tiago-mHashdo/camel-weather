package org.acme.camel.constants;

import org.acme.camel.utils.AppUtils;

public class GeneralConstants {

    // Properties
    public static final String ORIGIN_PROPERTY = "originProperty";
    public static final String DESTINATION_PROPERTY = "destinationProperty";
    public static final String TRAVEL_DURATION_PROPERTY = "travelDurationProperty";
    public static final String ARRIVAL_INSTANT_PROPERTY = "arrivalInstantProperty";
    public static final String DESTINATION_LATITUDE_PROPERTY = "destinationLatitudeProperty";
    public static final String DESTINATION_LONGITUDE_PROPERTY = "destinationLongitudeProperty";
    public static final String TEMPERATURE_PROPERTY = "temperatureProperty";
    public static final String EXCHANGE_MESSAGE_PROPERTY = "exchangeMessageProperty";

    // Exchange properties
    public static final String ORIGIN_EXCHANGE_PROPERTY = AppUtils.getExchangeProperty(ORIGIN_PROPERTY);
    public static final String DESTINATION_EXCHANGE_PROPERTY = AppUtils.getExchangeProperty(DESTINATION_PROPERTY);
    public static final String TRAVEL_DURATION_EXCHANGE_PROPERTY = AppUtils.getExchangeProperty(TRAVEL_DURATION_PROPERTY);
    public static final String ARRIVAL_INSTANT_EXCHANGE_PROPERTY = AppUtils.getExchangeProperty(ARRIVAL_INSTANT_PROPERTY);
    public static final String DESTINATION_LATITUDE_EXCHANGE_PROPERTY = AppUtils.getExchangeProperty(DESTINATION_LATITUDE_PROPERTY);
    public static final String DESTINATION_LONGITUDE_EXCHANGE_PROPERTY = AppUtils.getExchangeProperty(DESTINATION_LONGITUDE_PROPERTY);
    public static final String TEMPERATURE_EXCHANGE_PROPERTY = AppUtils.getExchangeProperty(TEMPERATURE_PROPERTY);
    public static final String EXCHANGE_MESSAGE_EXCHANGE_PROPERTY = AppUtils.getExchangeProperty(EXCHANGE_MESSAGE_PROPERTY);

    // Constants
    public static final String ORIGIN_CONST = "origin";
    public static final String DESTINATION_CONST = "destination";
    public static final String BRIDGE_ENDPOINT_TRUE_CONST = "?bridgeEndpoint=true";

    // Endpoints
    public static final String VALIDATE_LOCATIONS_ENDPOINT = "validateLocationsEndpoint";
    public static final String GET_WEATHER_FORECAST_ENDPOINT = "getWeatherForecastEndpoint";
    public static final String GET_TRAVEL_DURATION_AND_COORDINATES_ENDPOINT = "getTravelDurationAndCoordinates";

    // Route ids
    public static final String VALIDATE_LOCATIONS_ROUTE_ID = "validateLocationsRoute";
    public static final String GET_WEATHER_FORECAST_ROUTE = "getWeatherForecastRoute";
    public static final String GET_TRAVEL_DURATION_AND_COORDINATES_ROUTE = "getTravelDurationAndCoordinatesRoute";
    public static final String MAIN_ROUTE = "standardRestRoute";

    // Others
    public static final String ENDED_ROUTE_LOG = "|- Ended route:";
    public static final String STARTED_ROUTE_LOG = "|- Started route: ";
    public static final String CAMEL_HTTP_HEADER_PATTERN = "CamelHttp*";



}
