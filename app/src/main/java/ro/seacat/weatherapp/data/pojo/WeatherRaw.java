package ro.seacat.weatherapp.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherRaw {

  @SerializedName("coord")
  private Coordinates coordinates;
  private List<Weather> weather;
  @SerializedName("main")
  private Temperature temperature;
  private Wind wind;
  @SerializedName("name")
  private String cityName;

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

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public class Wind {

    @SerializedName("speed")
    private Double speed;

    @SerializedName("deg")
    private Double degrees;

    public Wind(Double speed, Double degrees) {
      this.speed = speed;
      this.degrees = degrees;
    }

    public Double getSpeed() {
      return speed;
    }

    public void setSpeed(Double speed) {
      this.speed = speed;
    }

    public Double getDegrees() {
      return degrees;
    }

    public void setDegrees(Double degrees) {
      this.degrees = degrees;
    }
  }

  public class Coordinates {

    @SerializedName("lat")
    public Double latitude;

    @SerializedName("lon")
    public Double longitude;

    public Coordinates(Double latitude, Double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
    }

    public Double getLatitude() {
      return latitude;
    }

    public void setLatitude(Double latitude) {
      this.latitude = latitude;
    }

    public Double getLongitude() {
      return longitude;
    }

    public void setLongitude(Double longitude) {
      this.longitude = longitude;
    }
  }

  public class Weather {

    @SerializedName("main")
    private String currentCondition;
    private String icon;

    public Weather(String currentCondition, String icon) {
      this.currentCondition = currentCondition;
      this.icon = icon;
    }

    public String getCurrentCondition() {
      return currentCondition;
    }

    public void setCurrentCondition(String currentCondition) {
      this.currentCondition = currentCondition;
    }

    public String getIcon() {
      return icon;
    }

    public void setIcon(String icon) {
      this.icon = icon;
    }
  }

  public class Temperature {

    @SerializedName("temp")
    private Double temperature;

    public Temperature(Double temperature) {
      this.temperature = temperature;
    }

    public Double getTemperature() {
      return temperature;
    }

    public void setTemperature(Double temp) {
      this.temperature = temp;
    }
  }

}