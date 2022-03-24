package edu.cnm.deepdive.fieldnotes.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import edu.cnm.deepdive.fieldnotes.R;
import edu.cnm.deepdive.fieldnotes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private NavController navController;
  private AppBarConfiguration appBarConfiguration;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    navController = ((NavHostFragment) getSupportFragmentManager()
        .findFragmentById(R.id.nav_host_fragment))
        .getNavController();
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home,
        R.id.navigation_personnel, R.id.navigation_audit)
        .build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(binding.navView, navController);
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
  }
}