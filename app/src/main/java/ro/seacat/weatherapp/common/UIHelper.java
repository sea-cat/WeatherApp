package ro.seacat.weatherapp.common;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import ro.seacat.weatherapp.R;

public class UIHelper {

  public static void createDialog(Context context, int titleResource, int messageResource, DialogInterface.OnClickListener positiveActionListener, DialogInterface.OnClickListener negativeButtonListener) {
    new AlertDialog.Builder(context)
        .setTitle(titleResource)
        .setMessage(messageResource)
        .setPositiveButton(R.string.ok, positiveActionListener)
        .setNegativeButton(R.string.cancel, negativeButtonListener)
        .create()
        .show();
  }
}

