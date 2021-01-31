package ro.seacat.weatherapp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface APIInterface {
  @GET("weather/")
  Call<WeatherData> getByCity(@Query("q") String location, @Query("APPID") String appId, @Query("units") String units);

  @GET("weather/")
  Call<WeatherData> getByLatLong(@Query("lat") String latitude, @Query("lon") String longitude, @Query("APPID") String appId, @Query("units") String units);

  @GET("img/w/{imageName}")
  @Streaming
  Call<ResponseBody> downloadImage(@Path("imageName") String imageName);
}
