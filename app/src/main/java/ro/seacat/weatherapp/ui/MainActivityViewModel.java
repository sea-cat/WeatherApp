package ro.seacat.weatherapp.ui;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.seacat.weatherapp.BuildConfig;
import ro.seacat.weatherapp.R;
import ro.seacat.weatherapp.api.APIClient;
import ro.seacat.weatherapp.api.APIInterface;
import ro.seacat.weatherapp.api.NoConnectivityException;
import ro.seacat.weatherapp.common.ImageHelper;
import ro.seacat.weatherapp.data.AppDatabase;
import ro.seacat.weatherapp.data.WeatherDao;
import ro.seacat.weatherapp.data.WeatherData;
import ro.seacat.weatherapp.data.WeatherDataTranslator;
import ro.seacat.weatherapp.data.WeatherRaw;

public class MainActivityViewModel extends AndroidViewModel implements Callback<WeatherRaw> {

  private final APIInterface apiInterface;
  private final APIInterface apiImageInterface;
  private WeatherDao dao;

  private final MutableLiveData<WeatherData> liveWeather = new MutableLiveData<>();
  private final MutableLiveData<Bitmap> liveIcon = new MutableLiveData<>();
  private final MutableLiveData<Integer> displayError = new MutableLiveData<>();

  public MainActivityViewModel(@NonNull Application application) {
    super(application);

    apiInterface = APIClient.getClient(application).create(APIInterface.class);
    apiImageInterface = APIClient.getImageClient(application).create(APIInterface.class);
    dao = Room.databaseBuilder(application, AppDatabase.class, "weatherapp").build().weatherDao();

    getWeather(53.0349, -5.6234, false); //
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

  private void getWeather(double lat, double lon, boolean force) {
    Executors.newSingleThreadExecutor().execute(() -> {
      WeatherData weatherData = dao.findByLatLong(lat, lon);
      if (!force && weatherData != null && TimeUnit.HOURS.convert(Math.abs(new Date().getTime() - weatherData.lastFetched.getTime()), TimeUnit.MILLISECONDS) <= 24) {
        liveWeather.postValue(weatherData);
        getIcon(weatherData.icon);
        return;
      }

      apiInterface.getByLatLong(lat, lon, BuildConfig.OPEN_WEATHER_KEY, APIClient.UNIT).enqueue(this);
    });
  }

  private void downloadImage(String icon) {
    apiInterface.downloadImage(APIClient.BASE_IMAGE_URL + icon).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (!response.isSuccessful() || response.body() == null) {
          liveIcon.setValue(null);
          return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
          if (ImageHelper.writeResponseBody(getApplication(), response.body(), icon))
            liveIcon.postValue(BitmapFactory.decodeFile(ImageHelper.getFile(getApplication(), icon).toString()));
        });
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        liveIcon.setValue(null);
      }
    });
  }

  @Override
  public void onResponse(Call<WeatherRaw> call, Response<WeatherRaw> response) {
    if (!response.isSuccessful() || response.body() == null) {
      displayError.setValue(R.string.error_download);
      return;
    }

    WeatherData weatherData = WeatherDataTranslator.translate(response.body());
    weatherData.lastFetched = new Date();
    liveWeather.setValue(weatherData);
    Executors.newSingleThreadExecutor().execute(() -> {
      Log.e("XXX", "trying to save data");
      dao.empty();
      dao.insert(weatherData);
    });
    getIcon(response.body().getWeather().get(0).getIcon());
  }

  private void getIcon(String iconName) {
    String icon = iconName + ImageHelper.IMAGE_EXTENSION_PNG;
    if (ImageHelper.getFile(getApplication(), icon).exists())
      liveIcon.postValue(BitmapFactory.decodeFile(ImageHelper.getFile(getApplication(), icon).toString()));
    else
      downloadImage(icon);
  }

  @Override
  public void onFailure(Call<WeatherRaw> call, Throwable t) {
    displayError.setValue(t instanceof NoConnectivityException ? R.string.error_no_connectivity : R.string.error_download);
  }

}