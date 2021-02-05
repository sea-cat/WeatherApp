package ro.seacat.weatherapp.di;

import android.content.Context;

import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.seacat.weatherapp.api.NetworkConnectionInterceptor;
import ro.seacat.weatherapp.api.WeatherAPI;
import ro.seacat.weatherapp.data.AppDatabase;
import ro.seacat.weatherapp.data.WeatherDao;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

  @Provides
  @Singleton
  public Retrofit provideRetrofit(@ApplicationContext Context applicationContext) {
    return new Retrofit.Builder()
        .baseUrl(WeatherAPI.BASE_URL)
        .client(
            new OkHttpClient.Builder()
                .addInterceptor(new NetworkConnectionInterceptor(applicationContext))
                .build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(
            new GsonBuilder()
                .setLenient()
                .create())
        )
        .build();
  }

  @Provides
  @Singleton
  public WeatherAPI provideWeatherApi(Retrofit retrofit) {
    return retrofit.create(WeatherAPI.class);
  }

  @Provides
  @Singleton
  public AppDatabase provideAppDatabase(@ApplicationContext Context applicationContext) {
    return Room.databaseBuilder(applicationContext, AppDatabase.class, "weatherapp").build();
  }

  @Provides
  public WeatherDao provideWeatherDao(AppDatabase database) {
    return database.weatherDao();
  }
}