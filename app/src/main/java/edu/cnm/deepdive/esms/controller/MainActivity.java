package edu.cnm.deepdive.esms.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.esms.R;
import edu.cnm.deepdive.esms.databinding.ActivityMainBinding;
import edu.cnm.deepdive.esms.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private NavController navController;
  private AppBarConfiguration appBarConfiguration;
  private UserViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setupViewModel();
    setupNavigation();
  }

  private void setupViewModel() {
    viewModel = new ViewModelProvider(this).get(UserViewModel.class);
    getLifecycle().addObserver(viewModel);
    viewModel
        .getAccount()
        .observe(this, this::handleAccount);
    viewModel.fetchCurrentUser();
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
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
  }

  private void setupNavigation() {
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_main_frag)
        .build();
    navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
  }
}