package ro.seacat.weatherapp.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;
import android.location.Location;
import android.text.format.DateFormat;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ro.seacat.weatherapp.R;
import ro.seacat.weatherapp.api.NoConnectivityException;
import ro.seacat.weatherapp.api.WeatherAPI;
import ro.seacat.weatherapp.common.Utils;
import ro.seacat.weatherapp.data.WeatherRepository;
import ro.seacat.weatherapp.data.pojo.WeatherData;

@HiltViewModel
public class MainActivityViewModel extends ViewModel {

  private final WeatherRepository repository;
  private final Application applicationContext;
  private final Utils utils;

  private final MutableLiveData<WeatherData> liveWeather = new MutableLiveData<>();
  private final MutableLiveData<Bitmap> liveIcon = new MutableLiveData<>();
  private final MutableLiveData<Integer> displayError = new MutableLiveData<>();
  private final MutableLiveData<String> displayLastUpdatedMessage = new MutableLiveData<>();
  private final MutableLiveData<Void> clearError = new MutableLiveData<>();
  private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);

  private final CompositeDisposable disposables = new CompositeDisposable();

  @Inject
  public MainActivityViewModel(WeatherRepository repository, Application applicationContext, Utils utils) {
    this.repository = repository;
    this.applicationContext = applicationContext;
    this.utils = utils;
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

  public LiveData<Void> getClearError() {
    return clearError;
  }

  public LiveData<Boolean> getLoading() {
    return loading;
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    disposables.clear();
  }

  public void fetchData(Location location) {
    loading.setValue(true);

    disposables.add(
        repository.fetchData(location)
            .subscribeOn(Schedulers.io())
            .doOnSuccess(weatherDataResponse -> {
              if (weatherDataResponse.getError() != null)
                displayError.postValue(weatherDataResponse.getError() instanceof NoConnectivityException ? R.string.error_no_connectivity : R.string.error_download);

              onResponse(weatherDataResponse.getWeatherData(), weatherDataResponse.isFromStorage());
            })
            .subscribe()
    );
  }

  private void onResponse(WeatherData weatherData, boolean fromStorage) {
    loading.postValue(false);
    if (weatherData == null)
      return;

    liveWeather.postValue(weatherData);
    displayLastUpdatedMessage.postValue(!fromStorage ? null : utils.formatWarning(applicationContext, weatherData));

    if (!fromStorage)
      clearError.postValue(null);
  }

  public String getWeatherIconUrl(String weatherDataIcon) {
    return WeatherAPI.ICON_URL + weatherDataIcon + ".png";
  }

}