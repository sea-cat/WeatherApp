package ro.seacat.weatherapp.data.util;

import java.util.Date;

import androidx.room.TypeConverter;

public class Converters {

  @TypeConverter
  public Date fromTimestamp(Long value) {
    return value == null ? null : new Date(value);
  }

  @TypeConverter
  public Long dateToTimestamp(Date date) {
    return date == null ? null : date.getTime();
  }
}