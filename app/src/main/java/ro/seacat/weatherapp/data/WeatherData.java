package ro.seacat.weatherapp.data;

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
  public Float temperature;

  @ColumnInfo(name = "WE_WIND_SPEED")
  public Float windSpeed;

  @ColumnInfo(name = "WE_WIND_DEGREES")
  public Float windDegrees;

  @ColumnInfo(name = "WE_ICON")
  public String icon;

  @ColumnInfo(name = "WE_LATITUDE")
  public Float latitude;

  @ColumnInfo(name = "WE_LONGITUDE")
  public Float longitude;

  @ColumnInfo(name = "WE_LAST_FETCHED_DATE")
  public Date lastFetched;
}
