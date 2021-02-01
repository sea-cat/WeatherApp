package ro.seacat.weatherapp.data;

import com.google.gson.annotations.SerializedName;

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
