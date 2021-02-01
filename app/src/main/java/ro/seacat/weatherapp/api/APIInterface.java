package ro.seacat.weatherapp.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import ro.seacat.weatherapp.data.WeatherRaw;

public interface APIInterface {

  @GET("weather/")
  Call<WeatherRaw> getByCity(@Query("q") String location, @Query("APPID") String appId, @Query("units") String units);

  @GET("weather/")
  Call<WeatherRaw> getByLatLong(@Query("lat") double latitude, @Query("lon") double longitude, @Query("APPID") String appId, @Query("units") String units);

  @GET("img/w/{imageName}")
  @Streaming
  Call<ResponseBody> downloadImage(@Url String url, @Path("imageName") String imageName);

  @GET
  @Streaming
  Call<ResponseBody> downloadImage(@Url String url);
}
