package ro.seacat.weatherapp.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import ro.seacat.weatherapp.R;
import ro.seacat.weatherapp.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

  private MainActivityViewModel viewModel;
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.setLifecycleOwner(this);
    binding.setViewModel(viewModel);

    viewModel.getShowToast().observe(this, stringId -> {
//      showToast(stringId);
      Snackbar.make(binding.container, stringId, Snackbar.LENGTH_LONG).show();

    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_privacy_policy) {
      final Intent openPrivacyPolicyWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy_url)));

      if (getPackageManager().queryIntentActivities(openPrivacyPolicyWebIntent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0)
        startActivity(openPrivacyPolicyWebIntent);
      else
        Toast.makeText(this, R.string.error_unable_to_load_link, Toast.LENGTH_LONG).show();

      return true;
    }

    if (id == R.id.action_open_source_licences) {
      Toast.makeText(this, R.string.menu_action_open_source_licences, Toast.LENGTH_LONG).show();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


}