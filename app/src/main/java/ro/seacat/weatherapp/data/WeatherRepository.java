package ro.seacat.weatherapp.data;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
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

  // TODO properly use the repo
  //  public Single<WeatherDataResponse> fetchData(Location location) {
  //    if (location != null)
  //      refreshWeatherData(53.0349, -5.6234);
  //    else
  //      refreshWeatherData();
  //  }

  //  private void refreshWeatherData(double lat, double lon) {
  //    disposables.add(
  //        getNetworkWeather(lat, lon)
  //            .subscribe(
  //                weatherData -> onSuccess(weatherData, false),
  //                throwable -> {
  //                  if (liveWeather.getValue() == null)
  //                    getExistingWeather(lat, lon);
  //                  else
  //                    loading.postValue(false);
  //
  //                  displayError.postValue(throwable instanceof NoConnectivityException ? R.string.error_no_connectivity : R.string.error_download);
  //                })
  //    );
  //  }
  //
  //  private void getExistingWeather(double lat, double lon) {
  //    disposables.add(getStoredWeather(lat, lon)
  //        .subscribe(
  //            weatherData -> onSuccess(weatherData, true),
  //            throwable -> loading.postValue(false)
  //        )
  //    );
  //  }
  //
  //  private void getExistingWeather() {
  //    disposables.add(getStoredWeather()
  //        .subscribe(
  //            weatherData -> onSuccess(weatherData, true),
  //            throwable -> loading.postValue(false)
  //        )
  //    );
  //  }

  public Maybe<WeatherData> getStoredWeather(double lat, double lon) {
    return getStoredWeather(weatherDao.findByLatLong(lat, lon));
  }

  public Maybe<WeatherData> getStoredWeather() {
    return getStoredWeather(weatherDao.getData());
  }

  public Maybe<WeatherData> getStoredWeather(Single<WeatherData> weatherDataSingle) {
    return weatherDataSingle
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