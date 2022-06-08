package com.krab.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

	FusedLocationProviderClient fusedLocationProviderClient;

	ImageButton locateBTN, refreshBTN;
	TextView cityView, temperatureView;
	ImageView weatherImage;
	RecyclerView forecastBar;
	TextInputEditText inputCity;
	Button citySearchBTN;

	Weather currentWeather;
	ArrayList<Weather> forecastWeatherList;
	FutureWeatherAdapter futureWeatherAdapter;
	String cityName;

	double lat = 52.726053, lon = 21.087461;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		locateBTN = findViewById(R.id.Locate);
		refreshBTN = findViewById(R.id.Refresh);
		cityView = findViewById(R.id.City);
		temperatureView = findViewById(R.id.Temperature);
		weatherImage = findViewById(R.id.TemperatureIcon);
		forecastBar = findViewById(R.id.FutureWeather);
		inputCity = findViewById(R.id.EditCity);
		citySearchBTN = findViewById(R.id.CitySearch);

		futureWeatherAdapter = new FutureWeatherAdapter(this, forecastWeatherList);
		forecastBar.setAdapter(futureWeatherAdapter);

		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

		locateBTN.setOnClickListener(v -> {
			if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
				fusedLocationProviderClient.getLastLocation().addOnSuccessListener(task -> {
					lat = task.getLatitude();
					lon = task.getLongitude();
					refreshData(lat, lon);
				});
			} else {
				error("Brak uprawnień do lokalizacji");
				ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
			}
		});

		refreshBTN.setOnClickListener(v -> {
			refreshData(lat, lon);
		});

		citySearchBTN.setOnClickListener(v -> {
			String c = inputCity.getText().toString();
			if (c.isEmpty()) {
				refreshData(lat,lon);
			} else {
				Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
				try {
					List<Address> addresses = geocoder.getFromLocationName(c, 1);
					lat = addresses.get(0).getLatitude();
					lon = addresses.get(0).getLongitude();
					refreshData(lat, lon);
					cityView.setText(c);
				} catch (Exception e) {
					error("Couldn't set city: " + c);
				}
			}
			inputCity.setText("");
		});
		refreshData(lat, lon);

	}

	private void refreshData(double lat, double lon) {
		prepareData(lat, lon);
		String tempW = currentWeather.icon.substring(0, 2);

		switch (tempW) {
			case "01":    //clear sky
				weatherImage.setImageResource(R.mipmap.sun_foreground);
				break;
			case "02":    // few clouds
				weatherImage.setImageResource(R.mipmap.sun_clouds_foreground);
				break;
			case "03":    //mist
			case "04":    // broken clouds
			case "50":    // scattered clouds
				weatherImage.setImageResource(R.mipmap.cloud_foreground);
				break;
			case "09":    //rain
			case "10":    // shower rain
				weatherImage.setImageResource(R.mipmap.rain_foreground);
				break;
			case "11":    //thunderstorm
				weatherImage.setImageResource(R.mipmap.storm_foreground);
				break;
			case "13":    //snow
				weatherImage.setImageResource(R.mipmap.snow_foreground);
				break;
		}
		String temp = String.valueOf(Math.round(currentWeather.temp - 273.15));
		temp += "°C";
		temperatureView.setText(temp);
		cityView.setText(getCityName(lat,lon));

		futureWeatherAdapter.updateWeatherList(forecastWeatherList);
		futureWeatherAdapter.notifyDataSetChanged();
	}

	private void prepareData(double lat, double lon) {
		WeatherHandler handler = new WeatherHandler(lat, lon);
		try {
			currentWeather = handler.getWeatherStructure(WeatherHandler.weatherType.CURRENT).get(0);
			forecastWeatherList = handler.getWeatherStructure(WeatherHandler.weatherType.FORECAST);
			Log.e("CHUJ", forecastWeatherList.toString());
		} catch (Exception e) {
			error("Data error");
		}
	}

	private String getCityName(double lat, double lon) {
		String city = "";
		Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

			for (Address address : addresses) {
				String c = address.getLocality();
				if (c != null && !c.equals("")) {
					city = c;
				} else {
					error("City not found: " + city);
				}
			}
		} catch (IOException e) {
			error("City not found: " + city);
		}
		return city;
	}

	void error(String msg) {
		Log.e("ERROR", msg);
		Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();

	}

}