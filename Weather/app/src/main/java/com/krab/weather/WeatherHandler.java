package com.krab.weather;

import org.json.JSONObject;

public class WeatherHandler {

	double lat;
	double lon;
	String key = "4f2d384d1135983f349845f909985682";

	public WeatherHandler(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public JSONObject getCurrentWeatherData() {
		String URL = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + key;


	}

	public JSONObject getDailyWeatherData() {
		String URL = "https://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat + "&lon=" + lon + "&appid=" + key;


	}
}
