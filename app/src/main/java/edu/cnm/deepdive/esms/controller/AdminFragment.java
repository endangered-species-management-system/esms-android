package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import edu.cnm.deepdive.esms.R;
import edu.cnm.deepdive.esms.databinding.FragmentAdminBinding;
import edu.cnm.deepdive.esms.databinding.FragmentCaseBinding;
import org.jetbrains.annotations.NotNull;

public class AdminFragment extends PreferenceFragmentCompat {

  FragmentAdminBinding binding;

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.admin_pref, rootKey);
  }

  @NotNull
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentAdminBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
