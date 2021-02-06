package ro.seacat.weatherapp.data.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import ro.seacat.weatherapp.data.pojo.WeatherData;
import ro.seacat.weatherapp.data.pojo.WeatherRaw;

@Singleton
public class WeatherDataTranslator {

  @Inject
  public WeatherDataTranslator() {
  }

  public WeatherData translate(WeatherRaw weatherRaw) {
    if (weatherRaw == null)
      return null;

    WeatherData weatherData = new WeatherData();
    if (weatherRaw.getCoordinates() != null) {
      weatherData.longitude = weatherRaw.getCoordinates().getLongitude();
      weatherData.latitude = weatherRaw.getCoordinates().getLatitude();
    }

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

    weatherData.cityName = weatherRaw.getCityName();

    return weatherData;
  }
}
