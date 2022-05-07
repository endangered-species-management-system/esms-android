package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.tabs.TabLayoutMediator;
import edu.cnm.deepdive.esms.NavigationGraphDirections;
import edu.cnm.deepdive.esms.R;
import edu.cnm.deepdive.esms.adapter.VPAdapter;
import edu.cnm.deepdive.esms.databinding.ActivityMainBinding;
import edu.cnm.deepdive.esms.databinding.FragmentMainBinding;
import edu.cnm.deepdive.esms.viewmodel.LoginViewModel;
import org.jetbrains.annotations.NotNull;

public class MainFragment extends Fragment {

  private FragmentMainBinding binding;
  private VPAdapter vpAdapter;
  private final String[] titles = new String[]{"Species", "Team", "Evidence"};
  private LoginViewModel viewModel;
  private NavController navController;

  @Override
  public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentMainBinding.inflate(inflater, container, false);
    vpAdapter = new VPAdapter(this);
    binding.viewPager.setAdapter(vpAdapter);
    new TabLayoutMediator(binding.tabLayout, binding.viewPager,
        (tab, position) -> tab.setText(titles[position])).attach();
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view,
      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupViewModel();
    navController = Navigation.findNavController(binding.getRoot());
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu,
      @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.main_options, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled;
    int itemId = item.getItemId();
    if (itemId == R.id.sign_out) {
      viewModel.signOut();
      handled = true;
    } else if (itemId == R.id.settings) {
      navController.navigate(NavigationGraphDirections.openSettings());
      handled = true;
    } else if (itemId == R.id.admin) {
      navController.navigate(NavigationGraphDirections.openAdminMenu());
      handled = true;
    } else {
      handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }


  private void setupViewModel() {
    viewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
  }

}