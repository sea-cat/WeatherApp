package ro.seacat.weatherapp.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import ro.seacat.weatherapp.data.pojo.WeatherRaw;

public interface WeatherAPI {

  String BASE_URL = "https://api.openweathermap.org/data/2.5/";
  String BASE_IMAGE_URL = "https://openweathermap.org/";

  String ICON_URL = "https://openweathermap.org/img/w/";
  String UNIT = "metric";

  @GET("weather/")
  Call<WeatherRaw> getByCity(@Query("q") String location, @Query("APPID") String appId, @Query("units") String units);

  @GET("weather/")
  Observable<WeatherRaw> getByLatLong(@Query("lat") double latitude, @Query("lon") double longitude, @Query("APPID") String appId, @Query("units") String units);

  @GET("img/w/{imageName}")
  @Streaming
  Call<ResponseBody> downloadImage(@Url String url, @Path("imageName") String imageName);

  @GET
  @Streaming
  Call<ResponseBody> downloadImage(@Url String url);
}
