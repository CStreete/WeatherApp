package com.cameron.weatherapp;

import com.cameron.weatherapp.modals.*;
import com.cameron.weatherapp.modals.met.Timeseries;
import com.cameron.weatherapp.modals.met.service.MetService;
import com.cameron.weatherapp.modals.smhi.Properties;
import com.cameron.weatherapp.modals.smhi.TimeSeries;
import com.cameron.weatherapp.modals.smhi.service.SmhiService;
import com.cameron.weatherapp.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

@SpringBootApplication
public class WeatherApplication implements CommandLineRunner {
	@Autowired
	private ForecastService forecastService;



	public static void main(String[] args) {
		SpringApplication.run(WeatherApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		var scan = new Scanner(System.in);
		runMenu(scan);

	}
	private void runMenu(Scanner scan) throws IOException {
		while (true) {
			System.out.println("1. List all");
			System.out.println("2. Add prediction");
			System.out.println("3. Update");
			System.out.println("4. Fetch SMHI Predictions");
			System.out.println("5. Fetch YR Predictions");
			System.out.println("6. Get average temp");
			System.out.println("9. Exit");
			System.out.println("Action : ");
			int sel = scan.nextInt();
			if(sel == 1 ) {
				listPredictions();
			} else if (sel == 2 ) {
				addPrediction(scan);
			} else if (sel == 3) {
				updatePrediction(scan);
			}  else if (sel == 4) {
				fetchSmhiData();

			} else if (sel == 5) {
				fetchMetDta();
			} else if (sel == 6){
				showAverageTemp(scan);

			} else if (sel == 9 ) {
				break;
			}
		}

	}
	private void fetchMetDta() throws IOException {
		MetService metService = new MetService();
		forecastService.fetchAndSaveWeatherForecasts(metService);

	}
	private void showAverageTemp(Scanner scan) {
		System.out.println("Enter date yyyy-MM-dd:");
		String inputtedDate = scan.next();
		Map<Integer, Double> averageTemperatures = forecastService.calculateAverageTemperatureByDate(LocalDate.parse(inputtedDate));
		for (Map.Entry<Integer, Double> entry : averageTemperatures.entrySet()) {
			int hour = entry.getKey();
			double averageTemperature = entry.getValue();
			System.out.println("Hour: " + hour);
			System.out.println("Average Temp: " + averageTemperature + "C");
			System.out.println("----------------------------------------");
		}
	}
	private void fetchSmhiData () throws IOException {
		SmhiService smhiService = new SmhiService();
		forecastService.fetchAndSaveWeatherForecasts(smhiService);
}
	private void addPrediction(Scanner scan) throws IOException {
		var  forecast = new Forecast();
		forecast.setId(UUID.randomUUID());
		LocalDate date = null;

		while (date == null){
			System.out.println("Enter date yyyy-MM-dd:");
			String inputtedDate = scan.next();
			try {
				date = LocalDate.parse(inputtedDate);

			} catch (DateTimeParseException e){
				System.out.println("Invalid date format");
			}
			forecast.setPredictionDate(date);
		}

		System.out.println("Enter hour (0-24)");
		forecast.setPredictionHour(scan.nextInt());
		System.out.println("Enter temperature");
		forecast.setPredictionTemperature(scan.nextInt());
		System.out.println("Sky fall (0-6):");
        forecast.setSkyFall(scan.nextInt() > 0);
		forecast.setCreated(LocalDateTime.now());
		forecast.setDataSource(DataSource.Console);
		forecastService.save(forecast);
		System.out.println("Forecast added");


	}
	private void updatePrediction(Scanner scan) throws IOException {
		listPredictions();
		System.out.printf("Enter which forecast to update:");
		int num = scan.nextInt() ;
		var forecast = forecastService.getByIndex(num-1);
		System.out.printf("CURRENT VALUES:\n Date:%s\n Hour:%d\n Temp:%d\n ",
				forecast.getPredictionDate(),
				forecast.getPredictionHour(),
				forecast.getPredictionTemperature()
		);

		System.out.printf("New Temperature:");
		int temp = scan.nextInt() ;
		forecast.setPredictionTemperature(temp);
		forecast.setUpdated(LocalDateTime.now());
		forecastService.update(forecast);

	}
	private void listPredictions() {
		int num = 1;
		for(var forecast : forecastService.getForecasts()){
			System.out.println("-------------------------------------------");
			System.out.printf("%d \nDate:%s \nHour:%d \nTemperature:%d\n",
					num,
					forecast.getPredictionDate().toString(),
					forecast.getPredictionHour(),
					forecast.getPredictionTemperature()
			);
			num++;
		}

	}
}
