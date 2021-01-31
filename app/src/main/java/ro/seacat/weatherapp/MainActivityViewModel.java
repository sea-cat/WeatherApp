package ro.seacat.weatherapp;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel implements Callback<WeatherData> {

  private final APIInterface apiInterface;
  private final APIInterface apiImageInterface;

  private final MutableLiveData<WeatherData> liveWeather = new MutableLiveData<>();
  private final MutableLiveData<Bitmap> liveIcon = new MutableLiveData<>();
  private final MutableLiveData<Integer> showToast = new MutableLiveData<>();

  public MainActivityViewModel(@NonNull Application application) {
    super(application);

    apiInterface = APIClient.getClient(application).create(APIInterface.class);
    apiImageInterface = APIClient.getImageClient(application).create(APIInterface.class);
    getWeather();
  }

  public MutableLiveData<WeatherData> getLiveWeather() {
    return liveWeather;
  }

  public MutableLiveData<Bitmap> getLiveIcon() {
    return liveIcon;
  }

  public MutableLiveData<Integer> getShowToast() {
    return showToast;
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
            new Handler(Looper.getMainLooper()).post(() -> liveIcon.setValue(BitmapFactory.decodeFile(ImageHelper.getFile(getApplication(), icon).toString())));
        });
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        liveIcon.setValue(null);
      }
    });
  }

  @Override
  public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
    if (!response.isSuccessful() || response.body() == null) {
      showToast.setValue(R.string.error_download);
      return;
    }

    liveWeather.setValue(response.body());

    String icon = response.body().getWeather().get(0).getIcon() + ImageHelper.IMAGE_EXTENSION_PNG;
    if (ImageHelper.getFile(getApplication(), icon).exists())
      liveIcon.setValue(BitmapFactory.decodeFile(ImageHelper.getFile(getApplication(), icon).toString()));
    else
      downloadImage(icon);
  }

  @Override
  public void onFailure(Call<WeatherData> call, Throwable t) {
    showToast.setValue(t instanceof NoConnectivityException ? R.string.error_no_connectivity : R.string.error_download);
  }

}