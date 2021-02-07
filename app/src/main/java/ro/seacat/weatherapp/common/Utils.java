package ro.seacat.weatherapp.common;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.DecimalFormat;

import javax.inject.Inject;
import javax.inject.Singleton;

import ro.seacat.weatherapp.R;
import ro.seacat.weatherapp.data.pojo.WeatherData;

@Singleton
public class Utils {

  private final static String LOCATION_FORMAT = "#0.###";

  @Inject
  public Utils() {
  }

  public double formatCoordinates(double coordinate) {
    return Double.parseDouble(new DecimalFormat(LOCATION_FORMAT).format(coordinate));
  }

  public String formatWarning(Context applicationContext, WeatherData weatherData) {
    return applicationContext.getResources().getString(R.string.warning_last_fetched,
        weatherData.cityName,
        DateFormat.getTimeFormat(applicationContext).format(weatherData.lastFetched),
        DateFormat.getMediumDateFormat(applicationContext).format(weatherData.lastFetched));
  }
}
