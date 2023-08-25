package com.cameron.weatherapp;

import com.cameron.weatherapp.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class WeatherApplication {
	@Autowired
	private ForecastService forecastService;

	public static void main(String[] args) {
		SpringApplication.run(WeatherApplication.class, args);
	}
	public void run(String... args) throws Exception {
		var scan = new Scanner(System.in);
		runMenu(scan);


	}

	private void runMenu(Scanner scan){
		while (true) {
			System.out.println("1. List all");
			System.out.println("2. Add prediction");
			System.out.println("9. Exit");
			System.out.println("Action : ");
			int sel = scan.nextInt();
			if(sel == 1 ) {
				listPredictions();
			} else if (sel == 2 ) {
				addPrediction(scan);
			} else if (sel == 3) {
				updatePrediction(scan);
			} else if (sel == 9 ) {
				break;
			}
		}

	}

	private void addPrediction(Scanner scan) {

	}

	private void updatePrediction(Scanner scan) {

	}

	private void listPredictions() {

	}
}
