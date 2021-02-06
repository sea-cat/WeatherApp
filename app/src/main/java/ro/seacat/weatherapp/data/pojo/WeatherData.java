package ro.seacat.weatherapp.data.pojo;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WEATHER")
public class WeatherData {

  @PrimaryKey
  public int uid;

  @ColumnInfo(name = "WE_CURRENT_CONDITION")
  public String currentCondition;

  @ColumnInfo(name = "WE_TEMPERATURE")
  public Double temperature;

  @ColumnInfo(name = "WE_WIND_SPEED")
  public Double windSpeed;

  @ColumnInfo(name = "WE_WIND_DEGREES")
  public Double windDegrees;

  @ColumnInfo(name = "WE_ICON")
  public String icon;

  @ColumnInfo(name = "WE_LATITUDE")
  public Double latitude;

  @ColumnInfo(name = "WE_LONGITUDE")
  public Double longitude;

  @ColumnInfo(name = "WE_CITY_NAME")
  public String cityName;

  @ColumnInfo(name = "WE_LAST_FETCHED_DATE")
  public Date lastFetched;

  public String getIcon() {
    return icon;
  }
}
