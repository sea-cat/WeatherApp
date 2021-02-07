package ro.seacat.weatherapp.data;

import android.location.Location;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import ro.seacat.weatherapp.BuildConfig;
import ro.seacat.weatherapp.api.WeatherAPI;
import ro.seacat.weatherapp.data.pojo.WeatherData;
import ro.seacat.weatherapp.data.pojo.WeatherDataResponse;
import ro.seacat.weatherapp.data.util.WeatherDataTranslator;

@Singleton
public class WeatherRepository {

  private final WeatherAPI weatherAPI;
  private final WeatherDao weatherDao;
  private final WeatherDataTranslator translator;

  @Inject
  public WeatherRepository(WeatherAPI weatherAPI, WeatherDao weatherDao, WeatherDataTranslator translator) {
    this.weatherAPI = weatherAPI;
    this.weatherDao = weatherDao;
    this.translator = translator;
  }

  public Single<WeatherDataResponse> fetchData(Location location) {
    if (location == null)
      return getStoredWeather()
          .onErrorResumeNext(throwable -> Single.just(new WeatherDataResponse(null, null, true)));

    return getNetworkWeather(location.getLatitude(), location.getLongitude())
        .onErrorResumeNext(throwable ->
            getStoredWeather(location.getLatitude(), location.getLongitude())
                .doOnSuccess(weatherDataResponse -> weatherDataResponse.setError(throwable))
                .onErrorResumeNext(innerThrowable -> Single.just(new WeatherDataResponse(null, throwable, true))));
  }

  private Single<WeatherDataResponse> getNetworkWeather(double lat, double lon) {
    return weatherAPI.getByLatLong(lat, lon, BuildConfig.OPEN_WEATHER_KEY, WeatherAPI.UNIT)
        .map(weatherRaw -> {
          WeatherData weatherData = translator.translate(weatherRaw);
          weatherData.lastFetched = new Date();

          weatherDao.deleteByLatLong(weatherData.latitude, weatherData.longitude);
          weatherDao.insert(weatherData);

          return new WeatherDataResponse(weatherData, null, false);
        });
  }

  private Single<WeatherDataResponse> getStoredWeather(double lat, double lon) {
    return getStoredWeather(weatherDao.findByLatLong(lat, lon));
  }

  private Single<WeatherDataResponse> getStoredWeather() {
    return getStoredWeather(weatherDao.getData());
  }

  private Single<WeatherDataResponse> getStoredWeather(Single<WeatherData> weatherDataSingle) {
    return weatherDataSingle
        .filter(weatherData -> {
          boolean isValid = TimeUnit.HOURS.convert(Math.abs(new Date().getTime() - weatherData.lastFetched.getTime()), TimeUnit.MILLISECONDS) <= 24;
          if (!isValid)
            weatherDao.deleteByLatLong(weatherData.latitude, weatherData.longitude);
          return isValid;
        })
        .map(weatherData -> new WeatherDataResponse(weatherData, null, true))
        .toSingle();
  }

}