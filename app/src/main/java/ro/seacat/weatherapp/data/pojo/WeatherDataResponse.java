package ro.seacat.weatherapp.data.pojo;

public class WeatherDataResponse {

  private WeatherData weatherData;
  private Throwable error;
  private boolean fromStorage;

  public WeatherDataResponse(WeatherData weatherData, Throwable error, boolean fromStorage) {
    this.weatherData = weatherData;
    this.error = error;
    this.fromStorage = fromStorage;
  }

  public WeatherData getWeatherData() {
    return weatherData;
  }

  public Throwable getError() {
    return error;
  }

  public void setError(Throwable error) {
    this.error = error;
  }

  public boolean isFromStorage() {
    return fromStorage;
  }
}
