package ro.seacat.weatherapp.data;

import com.google.gson.annotations.SerializedName;

public class Temperature {

  @SerializedName("temp")
  private Float temperature;

  public Temperature(Float temperature) {
    this.temperature = temperature;
  }

  public Float getTemperature() {
    return temperature;
  }

  public void setTemperature(Float temp) {
    this.temperature = temp;
  }
}