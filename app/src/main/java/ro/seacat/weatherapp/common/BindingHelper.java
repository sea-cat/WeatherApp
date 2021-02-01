package ro.seacat.weatherapp.common;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import ro.seacat.weatherapp.R;

public class BindingHelper {

  @BindingAdapter({"src"})
  public static void setBitmap(ImageView imageView, Bitmap bitmap) {
    if (bitmap == null)
      imageView.setImageDrawable(null);
    else
      imageView.setImageBitmap(bitmap);
  }

  @BindingAdapter({"temperature"})
  public static void setTemperature(TextView view, Double temperature) {
    view.setText(temperature == null
        ? view.getResources().getString(R.string.error_not_available)
        : view.getResources().getString(R.string.value_degree_celsius, Math.round(temperature)));
  }

  @BindingAdapter({"speed"})
  public static void setSpeed(TextView view, Double speed) {
    //value comes down in metric, requirement was to display mph, so doing the transformation
    view.setText(speed == null
        ? view.getResources().getString(R.string.error_not_available)
        : view.getResources().getString(R.string.value_miles_per_hour, Math.round(speed / 1.609)));
  }

  @BindingAdapter({"windDirection"})
  public static void setWindDirection(TextView view, Double degrees) {
    int directionTextId;
    if (degrees == null || degrees > 360 || degrees < 0)
      directionTextId = R.string.error_not_available;
    else if (degrees > 337.5 && degrees <= 22.5)
      directionTextId = R.string.value_north;
    else if (degrees > 22.5 && degrees <= 67.5)
      directionTextId = R.string.value_north_east;
    else if (degrees > 67.5 && degrees <= 112.5)
      directionTextId = R.string.value_east;
    else if (degrees > 112.5 && degrees <= 157.5)
      directionTextId = R.string.value_south_east;
    else if (degrees > 157.5 && degrees <= 202.5)
      directionTextId = R.string.value_south;
    else if (degrees > 202.5 && degrees <= 247.5)
      directionTextId = R.string.value_south_west;
    else if (degrees > 247.5 && degrees <= 292.5)
      directionTextId = R.string.value_west;
    else if (degrees > 292.5 && degrees <= 337.5)
      directionTextId = R.string.value_north_west;
    else
      directionTextId = R.string.error_not_available;

    view.setText(view.getResources().getString(directionTextId));
  }
}
