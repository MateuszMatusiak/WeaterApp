package com.krab.weather;

import java.util.Date;

public class Weather {
	double lat, lon;
	double temp;
	Date date;
	String description;
	String icon;

	public Weather(double lat, double lon, double temp, Date date, String description, String icon) {
		this.lat = lat;
		this.lon = lon;
		this.temp = temp;
		this.date = date;
		this.description = description;
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "Weather{" +
				"lat=" + lat +
				", lon=" + lon +
				", temp=" + temp +
				", date=" + date +
				", description='" + description + '\'' +
				", icon='" + icon + '\'' +
				'}';
	}
}
