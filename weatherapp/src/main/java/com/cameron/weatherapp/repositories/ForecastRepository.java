package com.cameron.weatherapp.repositories;

import com.cameron.weatherapp.modals.DataSource;
import com.cameron.weatherapp.modals.Forecast;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ForecastRepository extends CrudRepository<Forecast, UUID> {
    @Override
    List<Forecast> findAll();
    List<Forecast> findAllByPredictionDate(LocalDate predictionDate);

    List<Forecast> findAllByPredictionDateAndDataSource (LocalDate predictionDate, DataSource dataSource);







}
