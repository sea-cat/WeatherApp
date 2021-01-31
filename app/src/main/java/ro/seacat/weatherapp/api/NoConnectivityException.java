package ro.seacat.weatherapp.api;

import java.io.IOException;

public class NoConnectivityException extends IOException {

  @Override
  public String getMessage() {
    return "No Internet Connection";
  }
}