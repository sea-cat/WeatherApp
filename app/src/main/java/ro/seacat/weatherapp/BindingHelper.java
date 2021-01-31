package ro.seacat.weatherapp;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class BindingHelper {

  @BindingAdapter({"src"})
  public static void setBitmap(ImageView view, Bitmap bitmap) {
    view.setImageBitmap(bitmap);
  }
}
