package com.cameron.weatherapp.modals;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TimeSeries {
    @JsonProperty("validTime")
    private String validTime;
    @JsonProperty("parameters")
    private List<Properties> parameters;

    @JsonProperty("validTime")
    public String getValidTime() {
        return validTime;
    }

    @JsonProperty("validTime")
    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    @JsonProperty("parameters")
    public List<Properties> getParameters() {
        return parameters;
    }

    @JsonProperty("parameters")
    public void setParameters(List<Properties> parameters) {
        this.parameters = parameters;
    }
}
