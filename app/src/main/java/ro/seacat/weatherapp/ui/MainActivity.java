package ro.seacat.weatherapp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import ro.seacat.weatherapp.R;
import ro.seacat.weatherapp.databinding.ActivityMainBinding;
import ro.seacat.weatherapp.viewmodels.MainActivityViewModel;

@AndroidEntryPoint
public class MainActivity extends BaseActivity {

  private MainActivityViewModel viewModel;
  private ActivityMainBinding binding;
  private FusedLocationProviderClient fusedLocationClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    viewModel.getDisplayError().observe(this, stringId -> showSnackBar(binding.container, stringId));
    viewModel.getLiveWeather().observe(this, weatherData -> loadIcon(weatherData.icon));
    viewModel.getClearError().observe(this, nothing -> hideSnackBar());

    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.setLifecycleOwner(this);
    binding.setViewModel(viewModel);
    binding.fab.setOnClickListener(v -> checkLocationPermission());

    checkLocationPermission();
  }

  private void loadIcon(String icon) {
    Picasso.get()
        .load(viewModel.getWeatherIconUrl(icon))
        .fit()
        .placeholder(R.drawable.animation_progress)
        .error(R.drawable.ic_error)
        .into(binding.constraint.icon);
  }

  @SuppressLint("MissingPermission")
  @Override
  protected void locationPermissionGranted() {
    super.locationPermissionGranted();
    fusedLocationClient
        .getLastLocation()
        .addOnFailureListener(this, e -> {
          viewModel.fetchData(null);
          showSnackBar(binding.container, R.string.error_location_not_found);
        })
        .addOnSuccessListener(this, location -> {
          viewModel.fetchData(location);
          if (location == null)
            showSnackBar(binding.container, R.string.error_location_not_found);
        })
    ;
  }

  @Override
  protected void locationPermissionDenied() {
    super.locationPermissionDenied();
    viewModel.fetchData(null);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (R.id.action_privacy_policy == id) {
      final Intent openPrivacyPolicyWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy_url)));

      if (getPackageManager().queryIntentActivities(openPrivacyPolicyWebIntent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0)
        startActivity(openPrivacyPolicyWebIntent);
      else
        showToast(R.string.error_unable_to_load_link);

      return true;
    }

    if (R.id.action_open_source_licences == id) {
      showToast(R.string.menu_action_open_source_licences);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

}