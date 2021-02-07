package ro.seacat.weatherapp.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ro.seacat.weatherapp.data.pojo.WeatherRaw;

public interface WeatherAPI {

  String BASE_URL = "https://api.openweathermap.org/data/2.5/";
  String ICON_URL = "https://openweathermap.org/img/w/";
  String UNIT = "metric";

  @GET("weather/")
  Single<WeatherRaw> getByLatLong(@Query("lat") double latitude, @Query("lon") double longitude, @Query("APPID") String appId, @Query("units") String units);
}
