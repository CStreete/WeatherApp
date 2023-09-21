package com.cameron.weatherapp.dto;

import java.time.LocalDate;

public class UserForecastDTO {

    private float Longitude;
    private float Latitude;
    private LocalDate PredictionDate;
    private int PredictionHour;
    private int PredictionTemperature;
    private boolean SkyFall;

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public LocalDate getPredictionDate() {
        return PredictionDate;
    }

    public void setPredictionDate(LocalDate predictionDate) {
        PredictionDate = predictionDate;
    }

    public int getPredictionHour() {
        return PredictionHour;
    }

    public void setPredictionHour(int predictionHour) {
        PredictionHour = predictionHour;
    }

    public int getPredictionTemperature() {
        return PredictionTemperature;
    }

    public void setPredictionTemperature(int predictionTemperature) {
        PredictionTemperature = predictionTemperature;
    }

    public boolean isSkyFall() {
        return SkyFall;
    }

    public void setSkyFall(boolean skyFall) {
        SkyFall = skyFall;
    }
}
