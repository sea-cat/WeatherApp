package ro.seacat.weatherapp.ui;

import android.app.Application;
import android.graphics.Bitmap;
import android.location.Location;
import android.text.format.DateFormat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;

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
  private final FusedLocationProviderClient fusedLocationClient;

  private final MutableLiveData<WeatherData> liveWeather = new MutableLiveData<>();
  private final MutableLiveData<Bitmap> liveIcon = new MutableLiveData<>();
  private final MutableLiveData<Integer> displayError = new MutableLiveData<>();
  private final MutableLiveData<String> displayLastUpdatedMessage = new MutableLiveData<>();
  private final MutableLiveData<Void> clearError = new MutableLiveData<>();
  private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);

  private final CompositeDisposable disposables = new CompositeDisposable();

  @Inject
  public MainActivityViewModel(WeatherRepository repository, Application applicationContext) {
    this.repository = repository;
    this.applicationContext = applicationContext;

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext);
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
    repository.clearDisposable();
  }
  //
  //  public void fetchData(Location location) {
  //    repository.fetchData(location)
  //        .subscribeOn(Schedulers.io())
  //    .subscribe();
  //  }

  public void populateView(Location location) {
    loading.setValue(true);

    if (location != null && location.getLatitude() != 0.0) {
      normaliseLocation(location);
      refreshWeatherData(location);
    } else if (liveWeather.getValue() == null)
      getExistingWeather();
  }

  private void normaliseLocation(Location location) {
    location.setLatitude(Double.parseDouble(new DecimalFormat("#0.####").format(location.getLatitude())));
    location.setLongitude(Double.parseDouble(new DecimalFormat("#0.####").format(location.getLongitude())));
  }

  private void refreshWeatherData(Location location) {
    disposables.add(
        repository
            .getNetworkWeather(location.getLatitude(), location.getLongitude())
            .subscribe(
                weatherData -> onSuccess(weatherData, false),
                throwable -> {
                  if (liveWeather.getValue() == null)
                    getExistingWeather(location.getLatitude(), location.getLongitude());
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

    displayLastUpdatedMessage.postValue(!fromStorage ? null : applicationContext.getResources().getString(R.string.warning_last_fetched,
        weatherData.cityName,
        DateFormat.getTimeFormat(applicationContext).format(weatherData.lastFetched),
        DateFormat.getMediumDateFormat(applicationContext).format(weatherData.lastFetched)));

    if (!fromStorage)
      clearError.postValue(null);
  }

  public String getWeatherIconUrl(String weatherDataIcon) {
    return WeatherAPI.ICON_URL + weatherDataIcon + ".png";
  }
}