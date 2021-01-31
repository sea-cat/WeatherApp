package ro.seacat.weatherapp;

import android.app.Application;
import android.content.Context;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

  private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
  private static final String BASE_IMAGE_URL = "http://openweathermap.org/";

  private static Retrofit retrofit = null;
  private static Retrofit retrofitImage = null;

  static Retrofit getClient(Context context) {
    if (retrofit == null)
      retrofit = new Retrofit.Builder()
          .baseUrl(BASE_URL)
          .client(
              new OkHttpClient.Builder()
                  //                  .addInterceptor(
                  //                      new HttpLoggingInterceptor()
                  //                          .setLevel(HttpLoggingInterceptor.Level.BODY))
                  .addInterceptor(new NetworkConnectionInterceptor(context))
                  .build())
          .addConverterFactory(GsonConverterFactory.create(
              new GsonBuilder()
                  .setLenient()
                  .create())
          )
          .build();
    return retrofit;
  }

  static Retrofit getImageClient(Context context) {
    if (retrofitImage == null)
      retrofitImage = new Retrofit.Builder()
          .baseUrl(BASE_IMAGE_URL)
          .client(
              new OkHttpClient.Builder()
                  .addInterceptor(new NetworkConnectionInterceptor(context))
                  .build())
          .addConverterFactory(GsonConverterFactory.create(
              new GsonBuilder()
                  .setLenient()
                  .create())
          )
          .build();
    return retrofitImage;
  }
}