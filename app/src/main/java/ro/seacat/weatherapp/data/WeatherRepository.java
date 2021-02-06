package ro.seacat.weatherapp.data;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ro.seacat.weatherapp.BuildConfig;
import ro.seacat.weatherapp.api.WeatherAPI;
import ro.seacat.weatherapp.data.pojo.WeatherData;
import ro.seacat.weatherapp.data.util.WeatherDataTranslator;

@Singleton
public class WeatherRepository {

  private final WeatherAPI weatherAPI;
  private final WeatherDao weatherDao;
  private final WeatherDataTranslator translator;
  private final CompositeDisposable disposables = new CompositeDisposable();

  @Inject
  public WeatherRepository(WeatherAPI weatherAPI, WeatherDao weatherDao, WeatherDataTranslator translator) {
    this.weatherAPI = weatherAPI;
    this.weatherDao = weatherDao;
    this.translator = translator;
  }

  public Maybe<WeatherData> getStoredWeather(double lat, double lon) {
    return weatherDao.findByLatLong(lat, lon)
        .subscribeOn(Schedulers.io())
        .filter(weatherData -> {
          boolean isValid = TimeUnit.HOURS.convert(Math.abs(new Date().getTime() - weatherData.lastFetched.getTime()), TimeUnit.MILLISECONDS) <= 24;
          if (!isValid)
            weatherDao.deleteByLatLong(weatherData.latitude, weatherData.longitude);
          return isValid;
        });
  }

  public Maybe<WeatherData> getStoredWeather() {
    return weatherDao.getData()
        .subscribeOn(Schedulers.io())
        .filter(weatherData -> {
          boolean isValid = TimeUnit.HOURS.convert(Math.abs(new Date().getTime() - weatherData.lastFetched.getTime()), TimeUnit.MILLISECONDS) <= 24;
          if (!isValid)
            weatherDao.deleteByLatLong(weatherData.latitude, weatherData.longitude);
          return isValid;
        });
  }

  public Observable<WeatherData> getNetworkWeather(double lat, double lon) {
    return weatherAPI.getByLatLong(lat, lon, BuildConfig.OPEN_WEATHER_KEY, WeatherAPI.UNIT)
        .subscribeOn(Schedulers.io())
        .map(weatherRaw -> {
          WeatherData weatherData = translator.translate(weatherRaw);
          weatherData.lastFetched = new Date();

          weatherDao.deleteByLatLong(weatherData.latitude, weatherData.longitude);
          weatherDao.insert(weatherData);

          return weatherData;
        });
  }

  public void clearDisposable() {
    disposables.clear();
  }
}
