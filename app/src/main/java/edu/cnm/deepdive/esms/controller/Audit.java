package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.cnm.deepdive.esms.databinding.FragmentAuditBinding;
import edu.cnm.deepdive.esms.databinding.FragmentPersonnelBinding;

public class Audit extends Fragment {

  private FragmentAuditBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentAuditBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}