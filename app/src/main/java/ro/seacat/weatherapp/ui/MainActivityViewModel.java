package ro.seacat.weatherapp.ui;

import android.app.Application;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;
import ro.seacat.weatherapp.R;
import ro.seacat.weatherapp.api.NoConnectivityException;
import ro.seacat.weatherapp.api.WeatherAPI;
import ro.seacat.weatherapp.data.WeatherRepository;
import ro.seacat.weatherapp.data.pojo.WeatherData;

@HiltViewModel
public class MainActivityViewModel extends ViewModel {

  private final WeatherRepository repository;
  private final Application applicationContext;

  private final MutableLiveData<WeatherData> liveWeather = new MutableLiveData<>();
  private final MutableLiveData<Bitmap> liveIcon = new MutableLiveData<>();
  private final MutableLiveData<Integer> displayError = new MutableLiveData<>();
  private final MutableLiveData<String> displayLastUpdatedMessage = new MutableLiveData<>();
  private final MutableLiveData<Void> hideSnackBar = new MutableLiveData<>();
  private final MutableLiveData<Boolean> loaded = new MutableLiveData<>(false);
  private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);

  private final CompositeDisposable disposables = new CompositeDisposable();

  @Inject
  public MainActivityViewModel(WeatherRepository repository, Application applicationContext) {
    this.repository = repository;
    this.applicationContext = applicationContext;
  }

  public LiveData<WeatherData> getLiveWeather() {
    return liveWeather;
  }

  public LiveData<Bitmap> getLiveIcon() {
    return liveIcon;
  }

  public LiveData<Integer> getDisplayError() {
    return displayError;
  }

  public LiveData<String> getDisplayLastUpdatedMessage() {
    return displayLastUpdatedMessage;
  }

  public LiveData<Void> getHideSnackBar() {
    return hideSnackBar;
  }

  public LiveData<Boolean> getLoaded() {
    return loaded;
  }

  public LiveData<Boolean> getLoading() {
    return loading;
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    repository.clearDisposable();
  }

  public void populateView() {
    boolean location = true;
    loading.setValue(true);

    if (location)
      refreshWeatherData(53.0349, -5.6234);
    else if (liveWeather.getValue() == null)
      getExistingWeather();
  }

  private void refreshWeatherData(double lat, double lon) {
    disposables.add(
        repository
            .getNetworkWeather(lat, lon)
            .subscribe(
                weatherData -> onSuccess(weatherData, false),
                throwable -> {
                  if (liveWeather.getValue() == null)
                    getExistingWeather(lat, lon);
                  else
                    loading.postValue(false);

                  displayError.postValue(throwable instanceof NoConnectivityException ? R.string.error_no_connectivity : R.string.error_download);
                })
    );
  }

  private void getExistingWeather(double lat, double lon) {
    disposables.add(
        repository.getStoredWeather(lat, lon)
            .subscribe(
                weatherData -> onSuccess(weatherData, true),
                throwable -> loading.postValue(false)
            )
    );
  }

  private void getExistingWeather() {
    disposables.add(
        repository.getStoredWeather()
            .subscribe(
                weatherData -> onSuccess(weatherData, true),
                throwable -> loading.postValue(false)
            )
    );
  }

  private void onSuccess(WeatherData weatherData, boolean fromStorage) {
    liveWeather.postValue(weatherData);
    loading.postValue(false);
    loaded.postValue(true);

    displayLastUpdatedMessage.postValue(!fromStorage ? null : applicationContext.getResources().getString(R.string.warning_last_fetched,
        weatherData.cityName,
        DateFormat.getTimeFormat(applicationContext).format(weatherData.lastFetched),
        DateFormat.getMediumDateFormat(applicationContext).format(weatherData.lastFetched)));

    if (!fromStorage)
      hideSnackBar.postValue(null);
  }

  public String getWeatherIconUrl(String weatherDataIcon) {
    return WeatherAPI.ICON_URL + weatherDataIcon + ".png";
  }
}