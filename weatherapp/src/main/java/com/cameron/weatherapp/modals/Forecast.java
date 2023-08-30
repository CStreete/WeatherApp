package com.cameron.weatherapp.modals;

import java.util.UUID;

public class Forecast {

    private UUID id;
    private int date;// 20230821
    private int hours;
    private float temp;
    private boolean skyFall;

    public boolean isSkyFall() {
        return skyFall;
    }

    public void setSkyFall(boolean skyFall) {
        this.skyFall = skyFall;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }
}
