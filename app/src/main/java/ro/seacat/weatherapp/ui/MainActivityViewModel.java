package ro.seacat.weatherapp.ui;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
import ro.seacat.weatherapp.data.WeatherData;
import ro.seacat.weatherapp.data.WeatherDataTranslator;
import ro.seacat.weatherapp.data.WeatherRaw;

public class MainActivityViewModel extends AndroidViewModel implements Callback<WeatherRaw> {

  private final APIInterface apiInterface;
  private final APIInterface apiImageInterface;

  private final MutableLiveData<WeatherData> liveWeather = new MutableLiveData<>();
  private final MutableLiveData<Bitmap> liveIcon = new MutableLiveData<>();
  private final MutableLiveData<Integer> displayError = new MutableLiveData<>();

  public MainActivityViewModel(@NonNull Application application) {
    super(application);

    apiInterface = APIClient.getClient(application).create(APIInterface.class);
    apiImageInterface = APIClient.getImageClient(application).create(APIInterface.class);
    getWeather();
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

  private void getWeather() {
    apiInterface.getByCity("Belfast, UK", BuildConfig.OPEN_WEATHER_KEY, "metric").enqueue(this);
  }

  private void downloadImage(String icon) {
    apiImageInterface.downloadImage(icon).enqueue(new Callback<ResponseBody>() {
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

    liveWeather.setValue(WeatherDataTranslator.translate(response.body()));

    String icon = response.body().getWeather().get(0).getIcon() + ImageHelper.IMAGE_EXTENSION_PNG;
    if (ImageHelper.getFile(getApplication(), icon).exists())
      liveIcon.setValue(BitmapFactory.decodeFile(ImageHelper.getFile(getApplication(), icon).toString()));
    else
      downloadImage(icon);
  }

  @Override
  public void onFailure(Call<WeatherRaw> call, Throwable t) {
    displayError.setValue(t instanceof NoConnectivityException ? R.string.error_no_connectivity : R.string.error_download);
  }

}