package edu.cnm.deepdive.fieldnotes.controller;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import edu.cnm.deepdive.fieldnotes.R;
import edu.cnm.deepdive.fieldnotes.databinding.ActivityMainBinding;
import edu.cnm.deepdive.fieldnotes.viewmodel.LoginViewModel;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private NavController navController;
  private AppBarConfiguration appBarConfiguration;
  private LoginViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setupViewModel();
    setUpNavigation();
  }

  private void setUpNavigation() {
    navController = ((NavHostFragment) getSupportFragmentManager()
        .findFragmentById(R.id.nav_host_fragment))
        .getNavController();
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home,
        R.id.navigation_personnel, R.id.navigation_audit)
        .build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(binding.navView, navController);
  }

  private void setupViewModel() {
    viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    getLifecycle().addObserver(viewModel);
    viewModel
        .getAccount()
        .observe(this, this::handleAccount);
  }

  private void handleAccount(GoogleSignInAccount account) {
    if (account == null) {
      Intent intent = new Intent(this, LoginActivity.class)
          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    } else {
      Toast
          .makeText(this, getString(R.string.display_name_format, account.getDisplayName()),
              Toast.LENGTH_LONG)
          .show();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.main_options, menu);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled;
    int itemId = item.getItemId();
    if (itemId == R.id.settings) {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
      handled = true;
    } else if (itemId == R.id.sign_out) {
      viewModel.signOut();
      handled = true;
    } else {
      handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
  }
}