package ro.seacat.weatherapp.data;

import java.util.ArrayList;
import java.util.Collections;

public class WeatherDataTranslator {

  public static WeatherData translate(WeatherRaw weatherRaw) {
    if (weatherRaw == null)
      return null;

    WeatherData weatherData = new WeatherData();
    if (weatherRaw.getWeather() != null && !weatherRaw.getWeather().isEmpty()) {
      weatherData.currentCondition = weatherRaw.getWeather().get(0).getCurrentCondition();
      weatherData.icon = weatherRaw.getWeather().get(0).getIcon();
    }

    if (weatherRaw.getTemperature() != null) {
      weatherData.temperature = weatherRaw.getTemperature().getTemperature();
    }

    if (weatherRaw.getWind() != null) {
      weatherData.windSpeed = weatherRaw.getWind().getSpeed();
      weatherData.windDegrees = weatherRaw.getWind().getDegrees();
    }

    return weatherData;
  }

  public static WeatherRaw translate(WeatherData weatherData) {
    if (weatherData == null)
      return null;

    WeatherRaw weatherRaw = new WeatherRaw();
    if (weatherData.currentCondition != null || weatherData.icon != null)
      weatherRaw.setWeather(new ArrayList<>(Collections.singletonList(new Weather(weatherData.currentCondition, weatherData.icon))));

    if (weatherData.temperature != null)
      weatherRaw.setTemperature(new Temperature(weatherData.temperature));

    if (weatherData.windSpeed != null || weatherData.windDegrees != null)
      weatherRaw.setWind(new Wind(weatherData.windSpeed, weatherData.windDegrees));

    return weatherRaw;
  }
}
