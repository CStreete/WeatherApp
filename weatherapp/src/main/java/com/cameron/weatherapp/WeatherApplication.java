package com.cameron.weatherapp;

import com.cameron.weatherapp.modals.Forecast;
import com.cameron.weatherapp.modals.Properties;
import com.cameron.weatherapp.modals.Smhi;
import com.cameron.weatherapp.modals.TimeSeries;
import com.cameron.weatherapp.service.ForecastService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
			System.out.println("9. Exit");
			System.out.println("Action : ");
			int sel = scan.nextInt();
			if(sel == 1 ) {
				listPredictions();
			} else if (sel == 2 ) {
				addPrediction(scan);
			} else if (sel == 3) {
				updatePrediction(scan);
			} else if (sel == 4) {
				showWeatherPrediction();
			} else if (sel == 9 ) {
				break;
			}
		}

	}

	private void showWeatherPrediction ( ) throws IOException {

		//String fetchedProduct = objectMapper.writeValueAsString(forecast.getTimeSeries().get(0).getParameters());
		//System.out.println(fetchedProduct);
		ZonedDateTime currentTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
		int currentIndex = -1;
		List<TimeSeries> timeSeriesList = forecastService.fetchSMHIData().getTimeSeries();
		for (int i = 0; i < timeSeriesList.size(); i++) {
			ZonedDateTime validTime2 = ZonedDateTime.parse(timeSeriesList.get(i).getValidTime());
			if (validTime2.isAfter(currentTime)) {
				currentIndex = i;
				break;
			}
		}

		if (currentIndex != -1){
			for (int i = currentIndex; i < currentIndex + 25 && i < timeSeriesList.size() ; i++) {
				TimeSeries timeSeries = timeSeriesList.get(i);
				Properties temperatureParameter = null;
				Properties skyfall = null;

			// Find the parameter with the name "t"

			for (Properties param : timeSeries.getParameters()) {
				if ("t".equals(param.getName())) {
					temperatureParameter = param;

				}
				if ("pcat".equals(param.getName())) {
					skyfall = param;
				}
			}

			// Print the "t" value and valid time
			if (temperatureParameter != null && skyfall != null) {
				List<Integer> temperatureValues = temperatureParameter.getValues();
				int skyfallValue = skyfall.getValues().get(0);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String validTime = timeSeries.getValidTime();
				ZonedDateTime zonedDateTime = ZonedDateTime.parse(validTime);
				String testTime = zonedDateTime.format(formatter);

				String[] dateAndTime = testTime.split(" ");
				String formattedDate = dateAndTime[0].replace("-","");
				String formattedTime = dateAndTime[1].split(":")[0];

				System.out.println("Date: " + formattedDate);
				System.out.println("Time: " + formattedTime);
				System.out.println("Temperature Value: " + temperatureValues.get(0));
				Forecast newForecast = new Forecast();
				newForecast.setTemp(temperatureValues.get(0));
				newForecast.setId(UUID.randomUUID());
				newForecast.setHours(Integer.parseInt(formattedTime));
				newForecast.setDate(Integer.parseInt(formattedDate));

				forecastService.save(newForecast);
				if (skyfallValue > 0) {
					System.out.println("Sky fall");
					newForecast.setSkyFall(true);
				} else {
					System.out.println("Clear Skies");
					newForecast.setSkyFall(false);
				}
			}
		}
			}

		}



	private void addPrediction(Scanner scan) throws IOException {
		var  forecast = new Forecast();
		forecast.setId(UUID.randomUUID());
		System.out.println("Enter day");
		forecast.setDate(scan.nextInt());
		System.out.println("Enter hour");
		forecast.setHours(scan.nextInt());
		System.out.println("Enter temperature");
		forecast.setTemp(scan.nextFloat());

		forecastService.save(forecast);
		System.out.println("Forecast added");
		listPredictions();

	}

	private void updatePrediction(Scanner scan) throws IOException {
		listPredictions();
		System.out.printf("Ange vilken du vill uppdatera:");
		int num = scan.nextInt() ;
		var forecast = forecastService.getByIndex(num-1);
		System.out.printf("%d %d CURRENT: %f %n",
				forecast.getDate(),
				forecast.getHours(),
				forecast.getTemp()
		);
		System.out.printf("Ange ny temp:");
		float temp = scan.nextFloat() ;
		forecast.setTemp(temp);
		forecastService.update(forecast);

	}

	private void listPredictions() {
		int num = 1;
		for(var forecast : forecastService.getForecasts()){
			System.out.printf("%d %d %d %f %n",
					num,
					forecast.getDate(),
					forecast.getHours(),
					forecast.getTemp()
			);
			num++;
		}

	}
}
