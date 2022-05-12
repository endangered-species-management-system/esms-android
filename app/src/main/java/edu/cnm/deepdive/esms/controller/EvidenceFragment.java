package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.esms.databinding.FragmentEvidenceBinding;

public class EvidenceFragment extends Fragment {

  private FragmentEvidenceBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentEvidenceBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    binding.addEvidence.setOnClickListener((v) -> Navigation
        .findNavController(binding.getRoot())
        .navigate(MainFragmentDirections.openEvidenceDialog())
    );
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }
}