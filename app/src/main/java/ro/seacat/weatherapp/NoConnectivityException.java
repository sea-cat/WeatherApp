package ro.seacat.weatherapp;

import java.io.IOException;

class NoConnectivityException extends IOException {

  @Override
  public String getMessage() {
    return "No Internet Connection";
  }
}