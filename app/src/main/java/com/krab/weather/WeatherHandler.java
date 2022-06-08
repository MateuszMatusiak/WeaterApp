package com.krab.weather;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WeatherHandler {

	private double lat = 52.726053;
	private double lon = 21.087461;
	private String key = "4f2d384d1135983f349845f909985682";

	public enum weatherType {
		CURRENT, FORECAST;
	}

	public WeatherHandler(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		try (InputStream is = new URL(url).openStream()) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			String jsonText = readAll(rd);
			return new JSONObject(jsonText);
		}
	}

	public static Map<String, Object> jsonToMap(String str) {
		return new Gson().fromJson(
				str, new TypeToken<HashMap<String, Object>>() {
				}.getType()
		);
	}

	private JSONObject getCurrentWeatherData() throws IOException, JSONException {
		String URL = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + key;
		return readJsonFromUrl(URL);
	}

	private JSONObject getDailyWeatherData() throws JSONException, IOException {
		String URL = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + key;
		return readJsonFromUrl(URL);
	}

	public ArrayList<Weather> getWeatherStructure(weatherType type) throws JSONException, IOException {
		String data;
		ArrayList<Weather> result = new ArrayList<>();
		switch (type) {
			case CURRENT: {
				data = getCurrentWeatherData().toString();

				Map<String, Object> respMap = jsonToMap(data);
				handleWeatherData(result, respMap);
			}
			case FORECAST: {
				data = getDailyWeatherData().toString();

				Map<String, Object> respMap = jsonToMap(data);
				ArrayList<LinkedTreeMap<String, Object>> list = (((ArrayList<LinkedTreeMap<String, Object>>) respMap.get("list")));

				for (LinkedTreeMap<String, Object> stringObjectLinkedTreeMap : list) {
					handleWeatherData(result, stringObjectLinkedTreeMap);
				}
			}
		}
		return result;
	}

	private void handleWeatherData(ArrayList<Weather> result, Map<String, Object> respMap) {
		Map<String, Object> mainMap = jsonToMap(Objects.requireNonNull(respMap.get("main")).toString());
		Map<String, Object> weatherMap = (((ArrayList<LinkedTreeMap<String, Object>>) Objects.requireNonNull(respMap.get("weather"))).get(0));

		result.add(new Weather(lat, lon, (double) Objects.requireNonNull(mainMap.get("temp")), new Date(Double.valueOf(Objects.requireNonNull(respMap.get("dt")).toString()).longValue() * 1000), Objects.requireNonNull(weatherMap.get("description")).toString(), Objects.requireNonNull(weatherMap.get("icon")).toString()));
	}

}

