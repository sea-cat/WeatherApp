package ro.seacat.weatherapp;

import com.google.gson.annotations.SerializedName;

public class Weather {

  @SerializedName("main")
  private String currentCondition;
  private String icon;

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
