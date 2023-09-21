package com.cameron.weatherapp.dto;

import com.cameron.weatherapp.modals.DataSource;

import java.time.LocalDate;

public class AverageTempDTO {
    private LocalDate date;
    private int hour;
    private double averageTemp;
    DataSource dataSource;

     public LocalDate getDate() {
        return date;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public double getAverageTemp() {
        return averageTemp;
    }

    public void setAverageTemp(double averageTemp) {
        this.averageTemp = averageTemp;
    }


}
