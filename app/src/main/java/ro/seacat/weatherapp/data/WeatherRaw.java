package ro.seacat.weatherapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherRaw {

  private List<Weather> weather;
  @SerializedName("main")
  private Temperature temperature;
  private Wind wind;

  public List<Weather> getWeather() {
    return weather;
  }

  public void setWeather(List<Weather> weather) {
    this.weather = weather;
  }

  public Temperature getTemperature() {
    return temperature;
  }

  public void setTemperature(Temperature temperature) {
    this.temperature = temperature;
  }

  public Wind getWind() {
    return wind;
  }

  public void setWind(Wind wind) {
    this.wind = wind;
  }
}
