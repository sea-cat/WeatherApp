package ro.seacat.weatherapp.data;

import com.google.gson.annotations.SerializedName;

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
