package com.cameron.weatherapp.modals.met;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Units {

    @JsonProperty("air_pressure_at_sea_level")
    private String airPressureAtSeaLevel;
    @JsonProperty("air_temperature")
    private String airTemperature;
    @JsonProperty("cloud_area_fraction")
    private String cloudAreaFraction;
    @JsonProperty("precipitation_amount")
    private String precipitationAmount;
    @JsonProperty("relative_humidity")
    private String relativeHumidity;
    @JsonProperty("wind_from_direction")
    private String windFromDirection;
    @JsonProperty("wind_speed")
    private String windSpeed;

    @JsonProperty("air_pressure_at_sea_level")
    public String getAirPressureAtSeaLevel() {
        return airPressureAtSeaLevel;
    }

    @JsonProperty("air_pressure_at_sea_level")
    public void setAirPressureAtSeaLevel(String airPressureAtSeaLevel) {
        this.airPressureAtSeaLevel = airPressureAtSeaLevel;
    }

    @JsonProperty("air_temperature")
    public String getAirTemperature() {
        return airTemperature;
    }

    @JsonProperty("air_temperature")
    public void setAirTemperature(String airTemperature) {
        this.airTemperature = airTemperature;
    }

    @JsonProperty("cloud_area_fraction")
    public String getCloudAreaFraction() {
        return cloudAreaFraction;
    }

    @JsonProperty("cloud_area_fraction")
    public void setCloudAreaFraction(String cloudAreaFraction) {
        this.cloudAreaFraction = cloudAreaFraction;
    }

    @JsonProperty("precipitation_amount")
    public String getPrecipitationAmount() {
        return precipitationAmount;
    }

    @JsonProperty("precipitation_amount")
    public void setPrecipitationAmount(String precipitationAmount) {
        this.precipitationAmount = precipitationAmount;
    }

    @JsonProperty("relative_humidity")
    public String getRelativeHumidity() {
        return relativeHumidity;
    }

    @JsonProperty("relative_humidity")
    public void setRelativeHumidity(String relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    @JsonProperty("wind_from_direction")
    public String getWindFromDirection() {
        return windFromDirection;
    }

    @JsonProperty("wind_from_direction")
    public void setWindFromDirection(String windFromDirection) {
        this.windFromDirection = windFromDirection;
    }

    @JsonProperty("wind_speed")
    public String getWindSpeed() {
        return windSpeed;
    }

    @JsonProperty("wind_speed")
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

}
