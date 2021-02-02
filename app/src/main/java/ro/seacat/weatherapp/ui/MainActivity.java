package ro.seacat.weatherapp.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
    binding.fab.setOnClickListener(v -> viewModel.getWeather(53.0349, -5.6234));

    viewModel.getDisplayError().observe(this, stringId -> {
      //      showToast(stringId);
      showSnackBar(binding.container, stringId) ;

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
        showToast(R.string.error_unable_to_load_link);

      return true;
    }

    if (id == R.id.action_open_source_licences) {
      showToast(R.string.menu_action_open_source_licences);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


}