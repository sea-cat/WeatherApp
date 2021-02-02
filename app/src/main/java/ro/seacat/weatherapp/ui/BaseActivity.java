package ro.seacat.weatherapp.ui;

import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import ro.seacat.weatherapp.R;

public class BaseActivity extends AppCompatActivity {

  private Toast toast;
  private Snackbar snackbar;

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
    if (snackbar != null)
      snackbar.dismiss();
    snackbar = Snackbar.make(view, stringId, Snackbar.LENGTH_INDEFINITE);
    snackbar.setAction(R.string.dismiss, v -> snackbar.dismiss());
    snackbar.show();
  }

  protected void showSnackBar(View view, String string) {
    if (snackbar != null)
      snackbar.dismiss();
    snackbar = Snackbar.make(view, string, Snackbar.LENGTH_INDEFINITE);
    snackbar.setAction(R.string.dismiss, v -> snackbar.dismiss());
    snackbar.show();
  }
}
