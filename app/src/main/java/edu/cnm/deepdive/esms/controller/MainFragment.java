package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.tabs.TabLayoutMediator;
import edu.cnm.deepdive.esms.R;
import edu.cnm.deepdive.esms.adapter.VPAdapter;
import edu.cnm.deepdive.esms.databinding.ActivityMainBinding;
import edu.cnm.deepdive.esms.databinding.FragmentMainBinding;
import org.jetbrains.annotations.NotNull;

public class MainFragment extends Fragment {

  private FragmentMainBinding binding;
  private VPAdapter vpAdapter;
  private final String[] titles = new String[]{"Species", "Team", "Evidence"};

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


}