package ro.seacat.weatherapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import ro.seacat.weatherapp.R;

public abstract class BaseActivity extends AppCompatActivity {

  protected final static int LOCATION_PERMISSION = 321;
  private static final String PACKAGE = "package";

  private Toast toast;

  protected void checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
      requestLocationPermission();
    else
      locationPermissionGranted();
  }

  public void requestLocationPermission() {
    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
      createLocationPermissionRequest();
      return;
    }

    UIHelper.createDialog(this,
        R.string.location_permission_title, R.string.location_permission_rationale,
        (dialog, which) -> createLocationPermissionRequest(), (dialog, which) -> dialog.dismiss());
  }

  private void createLocationPermissionRequest() {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode != LOCATION_PERMISSION)
      return;

    if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
      locationPermissionGranted();
    else
      locationPermissionDenied();
  }

  protected void locationPermissionGranted() {
    showToast("permission granted");
  }

  protected void locationPermissionDenied() {
    ViewGroup rootLayout = (ViewGroup) ((ViewGroup) (findViewById(android.R.id.content))).getChildAt(0);
    if (!(rootLayout instanceof CoordinatorLayout)) {
      showToast(R.string.error_location_permission_needed);
      return;
    }

    Snackbar.make(rootLayout, R.string.error_location_permission_needed, Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.settings, v -> {
          Intent settingsIntent = new Intent();
          settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
          settingsIntent.setData(Uri.fromParts(PACKAGE, getPackageName(), null));
          startActivity(settingsIntent);
        }).show();
  }

  protected void showToast(int stringId) {
    if (toast != null)
      toast.cancel();
    toast = Toast.makeText(this, stringId, Toast.LENGTH_LONG);
    toast.show();
  }

  protected void showToast(String string) {
    if (toast != null)
      toast.cancel();
    toast = Toast.makeText(this, string, Toast.LENGTH_LONG);
    toast.show();
  }

  protected void showSnackBar(View view, int stringId) {
    Snackbar snackbar = Snackbar.make(view, stringId, Snackbar.LENGTH_INDEFINITE);
    snackbar.setAction(R.string.dismiss, v -> snackbar.dismiss())
        .show();
  }

  protected void showSnackBar(View view, String string) {
    Snackbar snackbar = Snackbar.make(view, string, Snackbar.LENGTH_INDEFINITE);
    snackbar.setAction(R.string.dismiss, v -> snackbar.dismiss())
        .show();
  }
}
