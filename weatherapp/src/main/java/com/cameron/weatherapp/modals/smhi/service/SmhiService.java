package com.cameron.weatherapp.modals.smhi.service;

import com.cameron.weatherapp.modals.DataSource;
import com.cameron.weatherapp.modals.Forecast;
import com.cameron.weatherapp.modals.smhi.Properties;
import com.cameron.weatherapp.modals.smhi.TimeSeries;
import com.cameron.weatherapp.service.ForecastService;
import com.cameron.weatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class SmhiService implements WeatherService {

    @Autowired
    ForecastService forecastService;
    @Override
    public void showWeatherForeCast() throws IOException {
        ZonedDateTime currentTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
        int currentIndex = findCurrentIndex(currentTime);

        if (currentIndex != -1) {
            List<TimeSeries> timeSeriesList = forecastService.fetchSMHIData().getTimeSeries();

            int lastProcessedHour = -1;

            for (int i = currentIndex; i < currentIndex + 25 && i < timeSeriesList.size(); i++) {
                TimeSeries timeSeries = timeSeriesList.get(i);
                Properties temperatureParameter = null;
                Properties skyfall = null;


                for (Properties param : timeSeries.getParameters()) {
                    if ("t".equals(param.getName())) {
                        temperatureParameter = param;
                    }
                    if ("pcat".equals(param.getName())) {
                        skyfall = param;
                    }
                }

                if (temperatureParameter != null && skyfall != null) {
                    int currentHour = processTimeSeriesAndSaveForecast(timeSeries, temperatureParameter, skyfall, lastProcessedHour);
                    lastProcessedHour = currentHour;
                }
            }
        }
    }


    private int findCurrentIndex(ZonedDateTime currentTime) throws IOException {
        List<TimeSeries> timeSeriesList = forecastService.fetchSMHIData().getTimeSeries();
        for (int i = 0; i < timeSeriesList.size(); i++) {
            ZonedDateTime validTime2 = ZonedDateTime.parse(timeSeriesList.get(i).getValidTime());
            if (validTime2.isAfter(currentTime)) {
                return i;
            }
        }
        return -1;
    }


    private int processTimeSeriesAndSaveForecast(TimeSeries timeSeries, Properties temperatureParameter, Properties skyFall, int lastProcessedHour) throws IOException {
        List<Integer> temperatureValues = temperatureParameter.getValues();
        List<List<Double>> geometries = forecastService.fetchSMHIData().getGeometry().getCoordinates();

        double longitude = geometries.get(0).get(0);
        double lattitude = geometries.get(0).get(1);
        int skyfallValue = skyFall.getValues().get(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String validTime = timeSeries.getValidTime();
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(validTime);
        String testTime = zonedDateTime.format(formatter);

        String[] dateAndTime = testTime.split(" ");
        String formattedDate = dateAndTime[0];
        String formattedTime = dateAndTime[1].split(":")[0];

        int currentHour = Integer.parseInt(formattedTime);

        if (currentHour != lastProcessedHour) {

            Forecast newForecast = createForecast(temperatureValues.get(0), currentHour, formattedDate, skyfallValue, longitude,lattitude);

            forecastService.saveForecastIfNotDuplicate(newForecast);
        }
        return currentHour;
    }

    public Forecast createForecast(int temperature, int hour, String date, double skyFallValue, double longitude, double lattitude) {
        Forecast newForecast = new Forecast();
        newForecast.setPredictionTemperature(temperature);
        newForecast.setId(UUID.randomUUID());
        newForecast.setPredictionHour(hour);
        newForecast.setPredictionDate(LocalDate.parse(date));
        newForecast.setCreated(LocalDateTime.now());
        newForecast.setSkyFall(skyFallValue > 0);
        newForecast.setDataSource(DataSource.Smhi);
        newForecast.setLongitude((float) longitude);
        newForecast.setLatitude((float) lattitude);
        return newForecast;
    }



}

