package ro.seacat.weatherapp;

import com.google.gson.annotations.SerializedName;

public class Main {

  @SerializedName("temp")
  private float temperature;

  public float getTemperature() {
    return temperature;
  }

  public void setTemperature(float temp) {
    this.temperature = temp;
  }
}