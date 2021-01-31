package ro.seacat.weatherapp.ui;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

  private Toast toast;

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
}
