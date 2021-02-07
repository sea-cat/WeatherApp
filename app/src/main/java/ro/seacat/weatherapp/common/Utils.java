package ro.seacat.weatherapp.common;

import java.text.DecimalFormat;

public class Utils {

  private final static String LOCATION_FORMAT = "#0.###";

  public static double formatCoordinates(double coordinate) {
    return Double.parseDouble(new DecimalFormat(LOCATION_FORMAT).format(coordinate));
  }
}
