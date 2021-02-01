package ro.seacat.weatherapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherRaw {

  @SerializedName("coord")
  private Coordinates coordinates;
  private List<Weather> weather;
  @SerializedName("main")
  private Temperature temperature;
  private Wind wind;

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

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
