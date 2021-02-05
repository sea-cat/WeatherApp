package ro.seacat.weatherapp.data;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;
import ro.seacat.weatherapp.data.pojo.WeatherData;

@Dao
public interface WeatherDao {

  @Query("SELECT * FROM WEATHER WHERE WE_LATITUDE LIKE :latitude AND WE_LONGITUDE LIKE :longitude LIMIT 1")
  Single<WeatherData> findByLatLong(double latitude, double longitude);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(WeatherData weatherData);

  @Query("DELETE FROM WEATHER")
  void empty();

  @Query("DELETE FROM WEATHER WHERE WE_LATITUDE = :latitude AND WE_LONGITUDE = :longitude")
  void deleteByLatLong(String latitude, String longitude);
}