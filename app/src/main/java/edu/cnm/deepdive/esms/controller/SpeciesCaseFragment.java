package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.esms.databinding.FragmentSpeciesCaseBinding;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import org.jetbrains.annotations.NotNull;

public class SpeciesCaseFragment extends Fragment {

  private FragmentSpeciesCaseBinding binding;
  private SpeciesViewModel speciesViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentSpeciesCaseBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view,
      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    speciesViewModel = new ViewModelProvider(getActivity()).get(SpeciesViewModel.class);
    speciesViewModel
        .getSpecies()
        .observe(getViewLifecycleOwner(),(species) -> {
          // TODO Populate view objects on the screen based on species.
          binding.placeholderSpecies.setText(species.getNumber());
        });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}