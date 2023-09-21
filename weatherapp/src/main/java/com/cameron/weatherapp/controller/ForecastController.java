package com.cameron.weatherapp.controller;

import com.cameron.weatherapp.dto.NewForecastDTO;
import com.cameron.weatherapp.dto.AverageTempDTO;
import com.cameron.weatherapp.dto.UserForecastDTO;
import com.cameron.weatherapp.modals.DataSource;
import com.cameron.weatherapp.modals.Forecast;
import com.cameron.weatherapp.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class ForecastController {
    @Autowired
    ForecastService forecastService;
    @GetMapping("/api/forecasts")
    public ResponseEntity<List <Forecast>> getAll (){
        return new ResponseEntity<>(forecastService.getForecasts(), HttpStatus.OK);
    }
    @GetMapping("/api/forecasts/{id}")
    public ResponseEntity<Forecast> getByID (@PathVariable UUID id){
        Optional<Forecast> forecast = forecastService.get(id);
        if (forecast.isPresent()) return ResponseEntity.ok(forecast.get());
        return ResponseEntity.notFound().build();
    }


    @PutMapping("/api/forecasts/{id}")
    public ResponseEntity<Forecast> Update(@PathVariable UUID id, @RequestBody NewForecastDTO newForecastDTO) throws IOException {

        var forecast = new Forecast();
        forecast.setId(id);
        forecast.setPredictionDate(newForecastDTO.getPredictionDate());
        forecast.setPredictionTemperature(newForecastDTO.getPredictionTemperature());
        forecast.setPredictionHour(newForecastDTO.getPredictionHour());
        forecast.setUpdated(LocalDateTime.now());
        forecastService.update(forecast);
        return ResponseEntity.ok(forecast);
    }
    @GetMapping("/api/forecasts/average")
    public List<AverageTempDTO> getAverageTemperatureByDate(
            @RequestParam("date") String date) {
        LocalDate predictionDate = LocalDate.parse(date);
        Map<Integer, Double> averageTemperatures = forecastService.calculateAverageTemperatureByDate(predictionDate);
        List<AverageTempDTO> result = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : averageTemperatures.entrySet()) {
            int hour = entry.getKey();
            double averagetemp = entry.getValue();
            AverageTempDTO data = new AverageTempDTO();
            data.setDate(predictionDate);
            data.setHour(hour);
            data.setAverageTemp(averagetemp);
            result.add(data);
        }

        return result;
    }

    @PostMapping("/api/forecasts")
    public ResponseEntity<Forecast> createForecast (@RequestBody UserForecastDTO userForecast){
        Forecast forecast =  new Forecast();
        forecast.setId(UUID.randomUUID());
        forecast.setCreated(LocalDateTime.now());
        forecast.setPredictionTemperature(userForecast.getPredictionTemperature());
        forecast.setDataSource(DataSource.User);
        forecast.setPredictionDate(userForecast.getPredictionDate());
        forecast.setSkyFall(userForecast.isSkyFall());
        forecast.setUpdated(null);
        forecast.setPredictionHour(userForecast.getPredictionHour());
        forecast.setLongitude(userForecast.getLongitude());
        forecast.setLatitude(userForecast.getLatitude());
        Forecast newForecast = forecastService.save(forecast);
        return ResponseEntity.ok(newForecast);
    }

    @GetMapping("/api/forecasts/smhi/average")
    public List<AverageTempDTO> getAverageTemperatureByDateAndDataSourceSmhi (@RequestParam("date") String date){
        LocalDate predictionDate = LocalDate.parse(date);
        Map<Integer, Double> averageTemperatures = forecastService.calculateAverageTemperatureByDateAndDataSource(predictionDate, DataSource.Smhi);
        List<AverageTempDTO> result = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : averageTemperatures.entrySet()) {
            int hour = entry.getKey();
            double averagetemp = entry.getValue();
            AverageTempDTO data = new AverageTempDTO();
            data.setDate(predictionDate);
            data.setHour(hour);
            data.setAverageTemp(averagetemp);
            data.setDataSource(DataSource.Smhi);
            result.add(data);
        }

        return result;
    }
    @GetMapping("/api/forecasts/met/average")
    public List<AverageTempDTO> getAverageTemperatureByDateAndDataSourceMet (@RequestParam("date") String date){
        LocalDate predictionDate = LocalDate.parse(date);
        Map<Integer, Double> averageTemperatures = forecastService.calculateAverageTemperatureByDateAndDataSource(predictionDate, DataSource.Met);
        List<AverageTempDTO> result = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : averageTemperatures.entrySet()) {
            int hour = entry.getKey();
            double averagetemp = entry.getValue();
            AverageTempDTO data = new AverageTempDTO();
            data.setDate(predictionDate);
            data.setHour(hour);
            data.setAverageTemp(averagetemp);
            data.setDataSource(DataSource.Met);
            result.add(data);
        }

        return result;
    }
}








