package ro.seacat.weatherapp.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import ro.seacat.weatherapp.R;
import ro.seacat.weatherapp.databinding.ActivityMainBinding;

@AndroidEntryPoint
public class MainActivity extends BaseActivity {

  private MainActivityViewModel viewModel;
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    viewModel.getDisplayError().observe(this, stringId -> showSnackBar(binding.container, stringId));
    viewModel.getLiveWeather().observe(this, weatherData -> {
        Picasso.get()
            .load(viewModel.getWeatherIconUrl(weatherData.icon))
            .placeholder(R.drawable.animation_progress)
            .error(R.drawable.ic_error)
            .into(binding.icon);
    });

    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.setLifecycleOwner(this);
    binding.setViewModel(viewModel);
    binding.fab.setOnClickListener(v -> checkLocationPermission());

        viewModel.refreshWeatherData();
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