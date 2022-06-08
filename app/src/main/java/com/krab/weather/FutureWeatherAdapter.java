package com.krab.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class FutureWeatherAdapter extends RecyclerView.Adapter<FutureWeatherAdapter.ViewHolder> {
	private Context context;
	private ArrayList<Weather> weatherList;

	public FutureWeatherAdapter(Context context, ArrayList<Weather> weatherList) {
		this.context = context;
		this.weatherList = weatherList;
	}

	public void updateWeatherList(ArrayList<Weather> weatherList) {
		this.weatherList = weatherList;
	}

	@NonNull
	@Override
	public FutureWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.future_weather_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull FutureWeatherAdapter.ViewHolder holder, int position) {
		Weather weather = weatherList.get(position);
		Calendar cal = Calendar.getInstance();
		cal.setTime(weather.date);
		int dayOfWeekNo = cal.get(Calendar.DAY_OF_WEEK);
		String day = "";
		switch (dayOfWeekNo) {
			case Calendar.SUNDAY:
				day = "Niedziela: ";
				break;
			case Calendar.MONDAY:
				day = "Poniedziałek: ";
				break;
			case Calendar.TUESDAY:
				day = "Wtorek: ";
				break;
			case Calendar.WEDNESDAY:
				day = "Środa: ";
				break;
			case Calendar.THURSDAY:
				day = "Czwartek: ";
				break;
			case Calendar.FRIDAY:
				day = "Piątek: ";
				break;
			case Calendar.SATURDAY:
				day = "Sobota: ";
				break;
		}
		day += "\n";
		day += cal.get(Calendar.HOUR_OF_DAY);
		day += ":00";

		holder.day.setText(day);

		String tempW = weather.icon.substring(0, 2);

		switch (tempW) {
			case "01":    //clear sky
				holder.temperatureIcon.setImageResource(R.mipmap.sun_foreground);
				break;
			case "02":    // few clouds
				holder.temperatureIcon.setImageResource(R.mipmap.sun_clouds_foreground);
				break;
			case "03":    //mist
			case "04":    // broken clouds
			case "50":    // scattered clouds
				holder.temperatureIcon.setImageResource(R.mipmap.cloud_foreground);
				break;
			case "09":    //rain
			case "10":    // shower rain
				holder.temperatureIcon.setImageResource(R.mipmap.rain_foreground);
				break;
			case "11":    //thunderstorm
				holder.temperatureIcon.setImageResource(R.mipmap.storm_foreground);
				break;
			case "13":    //snow
				holder.temperatureIcon.setImageResource(R.mipmap.snow_foreground);
				break;
		}
		String temp = String.valueOf(Math.round(weather.temp - 273.15));
		temp += "°C";
		holder.temperature.setText(temp);
	}

	@Override
	public int getItemCount() {
		return weatherList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private TextView day;
		private TextView temperature;
		private ImageView temperatureIcon;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			day = itemView.findViewById(R.id.DayM);
			temperature = itemView.findViewById(R.id.TemperatureM);
			temperatureIcon = itemView.findViewById(R.id.TemperatureIconM);
		}
	}
}
