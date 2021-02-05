package ro.seacat.weatherapp.ui;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Date;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.seacat.weatherapp.R;
import ro.seacat.weatherapp.api.NoConnectivityException;
import ro.seacat.weatherapp.api.WeatherAPI;
import ro.seacat.weatherapp.common.ImageHelper;
import ro.seacat.weatherapp.data.WeatherDao;
import ro.seacat.weatherapp.data.WeatherRepository;
import ro.seacat.weatherapp.data.pojo.WeatherData;
import ro.seacat.weatherapp.data.pojo.WeatherRaw;

@HiltViewModel
public class MainActivityViewModel extends ViewModel {

  private WeatherRepository repository;
  private WeatherAPI apiInterface;
  private WeatherDao dao;

  private final MutableLiveData<WeatherData> liveWeather = new MutableLiveData<>();
  private final MutableLiveData<Bitmap> liveIcon = new MutableLiveData<>();
  private final MutableLiveData<Integer> displayError = new MutableLiveData<>();
  private final MutableLiveData<Boolean> loaded = new MutableLiveData<>(false);
  private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

  public ImageHelper imageHelper;
  private final CompositeDisposable disposables = new CompositeDisposable();

  @Inject
  public MainActivityViewModel(WeatherRepository repository, ImageHelper imageHelper) {
    this.repository = repository;
    this.imageHelper = imageHelper;
    //    apiInterface = APIClient.getClient(applicationContext).create(WeatherAPI.class);
    //    dao = Room.databaseBuilder(applicationContext, AppDatabase.class, "weatherapp").build().weatherDao();

    //    getExistingWeather(53.0349, -5.6234);
    //    repository.getStoredWeather(53.0349, -5.6234);
    //    repository.getWeather(53.0349, -5.6234);
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

  void refreshWeatherData() {
    disposables.add(
        repository
            .getNetworkWeather(53.0349, -5.6234)
            .subscribe(weatherData -> {
              weatherData.lastFetched = new Date();

              liveWeather.postValue(weatherData);
              loading.postValue(false);
              loaded.postValue(true);

              //TODO
              //      dao.empty();
              //      dao.insert(weatherData);
              //    getIcon(response.body().getWeather().get(0).getIcon());

            }, throwable -> {
              loading.postValue(false);
              displayError.setValue(throwable instanceof NoConnectivityException ? R.string.error_no_connectivity : R.string.error_download);
            }));
  }


  //  private void getExistingWeather(double lat, double lon) {
  //    loading.postValue(true);
  //    Executors.newSingleThreadExecutor().execute(() -> {
  //      WeatherData weatherData = dao.findByLatLong(lat, lon);
  //      if (weatherData != null && TimeUnit.HOURS.convert(Math.abs(new Date().getTime() - weatherData.lastFetched.getTime()), TimeUnit.MILLISECONDS) <= 24) {
  //        liveWeather.postValue(weatherData);
  //        getIcon(weatherData.icon);
  //        loaded.postValue(true);
  //        // todo - this was last fetched at date etc
  //      }
  //      loading.postValue(false);
  //    });
  //  }
  //
  //  public void getWeather(double lat, double lon) {
  //    loading.setValue(true);
  //    apiInterface.getByLatLong(lat, lon, BuildConfig.OPEN_WEATHER_KEY, APIClient.UNIT).enqueue(this);
  //  }
  //
  //  private void downloadImage(String icon) {
  //    apiInterface.downloadImage(APIClient.BASE_IMAGE_URL + icon).enqueue(new Callback<ResponseBody>() {
  //      @Override
  //      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
  //        if (!response.isSuccessful() || response.body() == null) {
  //          liveIcon.setValue(null);
  //          return;
  //        }
  //
  //        Executors.newSingleThreadExecutor().execute(() -> {
  //          if (imageHelper.writeResponseBody(response.body(), icon))
  //            liveIcon.postValue(BitmapFactory.decodeFile(imageHelper.getFile(icon).toString()));
  //        });
  //      }
  //
  //      @Override
  //      public void onFailure(Call<ResponseBody> call, Throwable t) {
  //        liveIcon.setValue(null);
  //      }
  //    });
  //  }
  //

  private void getIcon(String iconName) {
    //    String icon = iconName + ImageHelper.IMAGE_EXTENSION_PNG;
    //    if (imageHelper.getFile(icon).exists())
    //      liveIcon.postValue(BitmapFactory.decodeFile(imageHelper.getFile(icon).toString()));
    //    else
    //      downloadImage(icon);
  }
}