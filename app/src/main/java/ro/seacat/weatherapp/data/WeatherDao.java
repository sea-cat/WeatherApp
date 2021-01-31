package ro.seacat.weatherapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface WeatherDao {

  @Query("SELECT * FROM WEATHER WHERE WE_LATITUDE LIKE :latitude AND WE_LONGITUDE LIKE :longitude LIMIT 1")
  WeatherData findByLatLong(String latitude, String longitude);

  @Insert
  void insert(WeatherData weatherData);

  @Delete
  void delete(WeatherData weatherData);

  @Query("DELETE FROM WEATHER WHERE WE_LATITUDE = :latitude AND WE_LONGITUDE = :longitude")
  void deleteByLatLong(String latitude, String longitude);
}