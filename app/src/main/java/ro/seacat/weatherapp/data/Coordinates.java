package ro.seacat.weatherapp.data;

import com.google.gson.annotations.SerializedName;

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
