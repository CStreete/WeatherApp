package com.cameron.weatherapp.modals.met.service;

import com.cameron.weatherapp.modals.DataSource;
import com.cameron.weatherapp.modals.Forecast;
import com.cameron.weatherapp.modals.met.Data;

import com.cameron.weatherapp.modals.met.Met;
import com.cameron.weatherapp.modals.met.Timeseries;


import com.cameron.weatherapp.service.ForecastService;
import com.cameron.weatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
public class MetService implements WeatherService {
    @Autowired
    ForecastService forecastService;

    @Override
    public void showWeatherForeCast()  throws IOException {
        ZonedDateTime currentTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);

        int currentIndex = findCurrentIndex(currentTime);

        if (currentIndex != -1) {
            List<Timeseries> timeSeriesList = forecastService.fetchMetData().getProperties().getTimeseries();
            int lastProcessedHour = -1;

            for (int i = currentIndex; i < currentIndex + 25 && i < timeSeriesList.size(); i++) {

                Timeseries timeSeries = timeSeriesList.get(i);
                double temperatureParameter = timeSeries.getData().getInstant().getDetails().getAirTemperature();
                double skyfall = timeSeries.getData().getNext1Hours().getDetails().getPrecipitationAmount() ;

                    int currentHour = processTimeSeriesAndSaveForecast(timeSeries, temperatureParameter, skyfall, lastProcessedHour);
                    lastProcessedHour = currentHour;


            }
        }



    }


    private int findCurrentIndex(ZonedDateTime currentTime) throws IOException {
        List<Timeseries> timeSeriesList = forecastService.fetchMetData().getProperties().getTimeseries();
        for (int i = 0; i < timeSeriesList.size(); i++) {
            ZonedDateTime validTime2 = ZonedDateTime.parse(timeSeriesList.get(i).getTime());
            if (validTime2.isAfter(currentTime)) {
                return i;
            }
        }
        return -1;
    }


    private int processTimeSeriesAndSaveForecast(Timeseries timeSeries, double temperatureParameter, double skyFall, int lastProcessedHour) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String validTime = timeSeries.getTime();
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(validTime);
        String testTime = zonedDateTime.format(formatter);
        double longitude = forecastService.fetchMetData().getGeometry().getCoordinates().get(0);
        double latitude = forecastService.fetchMetData().getGeometry().getCoordinates().get(1);

        String[] dateAndTime = testTime.split(" ");
        String formattedDate = dateAndTime[0];
        String formattedTime = dateAndTime[1].split(":")[0];

        int currentHour = Integer.parseInt(formattedTime);

        if (currentHour != lastProcessedHour) {

            Forecast newForecast = createForecast((int) temperatureParameter, currentHour, formattedDate, skyFall, longitude,latitude);

            forecastService.saveForecastIfNotDuplicate(newForecast);
        }
        return currentHour;
    }

    private Forecast createForecast(int temperature, int hour, String date, double skyFallValue, double longitude, double latitude) {
        Forecast newForecast = new Forecast();
        newForecast.setPredictionTemperature(temperature);
        newForecast.setId(UUID.randomUUID());
        newForecast.setPredictionHour(hour);
        newForecast.setPredictionDate(LocalDate.parse(date));
        newForecast.setCreated(LocalDateTime.now());
        newForecast.setSkyFall(skyFallValue > 0);
        newForecast.setDataSource(DataSource.Met);
        newForecast.setLatitude((float) latitude);
        newForecast.setLongitude((float) longitude);
        return newForecast;
    }



}
