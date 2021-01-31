package ro.seacat.weatherapp.data;

import com.google.gson.annotations.SerializedName;

public class Wind {

  @SerializedName("speed")
  private Float speed;

  @SerializedName("deg")
  private Float degrees;

  public Wind(Float speed, Float degrees) {
    this.speed = speed;
    this.degrees = degrees;
  }

  public Float getSpeed() {
    return speed;
  }

  public void setSpeed(Float speed) {
    this.speed = speed;
  }

  public Float getDegrees() {
    return degrees;
  }

  public void setDegrees(Float degrees) {
    this.degrees = degrees;
  }
}
