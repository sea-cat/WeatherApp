package ro.seacat.weatherapp.data;

import com.google.gson.annotations.SerializedName;

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