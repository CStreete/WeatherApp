package com.cameron.weatherapp.service;

import com.cameron.weatherapp.modals.DataSource;
import com.cameron.weatherapp.modals.Forecast;
import com.cameron.weatherapp.modals.met.Met;
import com.cameron.weatherapp.modals.met.Properties;
import com.cameron.weatherapp.modals.smhi.Smhi;
import com.cameron.weatherapp.repositories.ForecastRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ForecastService {

    @Autowired
    private ForecastRepository forecastRepository;

    public List<Forecast> getForecasts(){
        return forecastRepository.findAll();
    }
    public Forecast save(Forecast forecast)  {

        forecastRepository.save(forecast);
        return forecast;
    }
    public Forecast getByIndex(int i) {
        return getForecasts().get(i);//forecasts.get(i);
    }
    public void update(Forecast forecastFromUser) throws IOException {
        var foreCastInList = get(forecastFromUser.getId()).get();
        foreCastInList.setPredictionTemperature(forecastFromUser.getPredictionTemperature());
        foreCastInList.setPredictionDate(forecastFromUser.getPredictionDate());
        foreCastInList.setPredictionHour(forecastFromUser.getPredictionHour());
        forecastRepository.save(foreCastInList);
    }
    public Smhi fetchSMHIData () throws IOException {
        var objectMapper = new ObjectMapper();
        Smhi fetchedForecasts = objectMapper.readValue(
                new URL("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/17.944219/lat/59.403945/data.json"), Smhi.class);
        return fetchedForecasts;
    }
    public Met fetchMetData () throws IOException {
        var objectMapper = new ObjectMapper();
        URL url = new URL("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=59.403945&lon=17.944219");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "WeatherApp/1.0 camestreete@hotmail.com");
        Met fetchedForecasts = objectMapper.readValue(connection.getInputStream(), Met.class );
        return fetchedForecasts;
    }
    public Optional<Forecast> get(UUID id) {
        return forecastRepository.findById(id);
    }
    public List<Forecast>getForecastByDate(LocalDate date){
        return forecastRepository.findAllByPredictionDate(date);
    }

    public List <Forecast>getForecastByDataAndDataSource(LocalDate localDate, DataSource dataSource){
        return forecastRepository.findAllByPredictionDateAndDataSource(localDate, dataSource);
    }

    public Map<Integer, Double> calculateAverageTemperatureByDate(LocalDate predictionDate) {
        List <Forecast> forecastList = getForecastByDate(predictionDate);

        Map<Integer, List<Forecast>> forecastsByHour = forecastList.stream()
                .collect(Collectors.groupingBy(Forecast::getPredictionHour));

        Map<Integer, Double> result = new HashMap<>();

        forecastsByHour.forEach((hour, forecasts) -> {
            double averageTemperature = forecasts.stream()
                    .mapToDouble(Forecast::getPredictionTemperature)
                    .average()
                    .orElse(0.0);
            result.put(hour, averageTemperature);

        });
        return result;
    }

    public void saveForecastIfNotDuplicate(Forecast newForecast) {
        List<Forecast> existingForecasts = getForecasts();
        boolean isDuplicate = false;
        for (Forecast e : existingForecasts) {
            if (e.getPredictionDate().isEqual(newForecast.getPredictionDate())
                    && e.getPredictionHour() == newForecast.getPredictionHour() && e.getDataSource() == newForecast.getDataSource()) {
                isDuplicate = true;
                break;
            }
        }
        if (!isDuplicate) {
            save(newForecast);
        }
    }

    public Map<Integer, Double> calculateAverageTemperatureByDateAndDataSource(LocalDate predictionDate, DataSource dataSource) {
        List <Forecast> forecastList = getForecastByDataAndDataSource(predictionDate, dataSource);
        Map<Integer, List<Forecast>> forecastsByHour = forecastList.stream()
                .collect(Collectors.groupingBy(Forecast::getPredictionHour));

        Map<Integer, Double> result = new HashMap<>();

        forecastsByHour.forEach((hour, forecasts) -> {
            double averageTemperature = forecasts.stream()
                    .mapToDouble(Forecast::getPredictionTemperature)
                    .average()
                    .orElse(0.0);
            result.put(hour, averageTemperature);

        });
        return result;
    }

    public void fetchAndSaveWeatherForecasts(WeatherService weatherService) throws IOException{
        weatherService.showWeatherForeCast();
    }













}






