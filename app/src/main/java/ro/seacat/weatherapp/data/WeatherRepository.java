package ro.seacat.weatherapp.data;

import android.util.Log;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.room.EmptyResultSetException;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ro.seacat.weatherapp.BuildConfig;
import ro.seacat.weatherapp.api.WeatherAPI;
import ro.seacat.weatherapp.data.pojo.WeatherData;
import ro.seacat.weatherapp.data.pojo.WeatherRaw;
import ro.seacat.weatherapp.data.util.WeatherDataTranslator;

@Singleton
public class WeatherRepository {

  private WeatherAPI weatherAPI;
  private WeatherDao weatherDao;
  private WeatherDataTranslator translator;
  private CompositeDisposable disposables = new CompositeDisposable();

  @Inject
  public WeatherRepository(WeatherAPI weatherAPI, WeatherDao weatherDao, WeatherDataTranslator translator) {
    this.weatherAPI = weatherAPI;
    this.weatherDao = weatherDao;
    this.translator = translator;
  }

  public void getStoredWeather(double lat, double lon) {
    //    disposables.add(
    //        weatherDao.findByLatLong(lat, lon)
    //            .subscribeOn(Schedulers.io())
    //            .observeOn(AndroidSchedulers.mainThread())
    //            .filter(weatherData -> TimeUnit.HOURS.convert(Math.abs(new Date().getTime() - weatherData.lastFetched.getTime()), TimeUnit.MILLISECONDS) <= 24)
    //            .subscribe(this::handleResults, this::handleError);
    //  );
    //
    disposables.add(
        weatherDao.findByLatLong(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(weatherData -> {
              Log.e("XXX", "getStoredWeather: " + weatherData);
            })
            .onErrorResumeNext(error -> {
              Log.e("XXX", "getStoredWeather: " + "ERROR!");
              error.printStackTrace();
              if (error instanceof EmptyResultSetException)
                return null;
              else
                return Single.error(error);
            })
            .filter(weatherData -> TimeUnit.HOURS.convert(Math.abs(new Date().getTime() - weatherData.lastFetched.getTime()), TimeUnit.MILLISECONDS) <= 24)
            .subscribe(this::handleResults, this::handleError)
    );

  }

  private void handleError(Throwable throwable) {
    Log.e("XXX", "handleError: " + "");
    throwable.printStackTrace();
  }

  private void handleResults(WeatherData weatherData) {
    Log.e("XXX", "handleResults: " + weatherData.currentCondition);
  }

//  public void getWeather(double lat, double lon) {
//    disposables.add(
//        weatherAPI.getByLatLong(lat, lon, BuildConfig.OPEN_WEATHER_KEY, WeatherAPI.UNIT)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .flatMap((Function<WeatherRaw, ObservableSource<WeatherData>>) weatherRaw -> Observable.just(translator.translate(weatherRaw)))
//            .subscribe(this::handleResults, this::handleError)
//    );
//  }

  public Observable<WeatherData> getNetworkWeather(double lat, double lon) {
    return weatherAPI.getByLatLong(lat, lon, BuildConfig.OPEN_WEATHER_KEY, WeatherAPI.UNIT)
        .subscribeOn(Schedulers.io())
        .flatMap((Function<WeatherRaw, ObservableSource<WeatherData>>) weatherRaw -> Observable.just(translator.translate(weatherRaw)));
  }


  public void clearDisposable() {
    disposables.clear();
  }
}
