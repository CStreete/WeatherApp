package com.cameron.weatherapp.dto;

import com.cameron.weatherapp.modals.DataSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class NewForecastDTO { // Data transfer object

    private LocalDateTime Created;
    private LocalDateTime Updated;
    private float Longitude;
    private float Latitude;
    private LocalDate PredictionDate;
    private int PredictionHour;
    private int PredictionTemperature;
    private boolean SkyFall;
    private DataSource DataSource;



    public LocalDateTime getCreated() {
        return Created;
    }

    public void setCreated(LocalDateTime created) {
        Created = created;
    }

    public LocalDateTime getUpdated() {
        return Updated;
    }

    public void setUpdated(LocalDateTime updated) {
        Updated = updated;
    }

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

    public com.cameron.weatherapp.modals.DataSource getDataSource() {
        return DataSource;
    }

    public void setDataSource(com.cameron.weatherapp.modals.DataSource dataSource) {
        DataSource = dataSource;
    }
}

