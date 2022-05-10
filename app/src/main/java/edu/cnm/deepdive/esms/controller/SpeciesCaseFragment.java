package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.esms.NavigationGraphDirections;
import edu.cnm.deepdive.esms.databinding.FragmentSpeciesCaseBinding;
import edu.cnm.deepdive.esms.model.entity.Species;
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import edu.cnm.deepdive.esms.viewmodel.UserViewModel;
import org.jetbrains.annotations.NotNull;

public class SpeciesCaseFragment extends Fragment {

  private FragmentSpeciesCaseBinding binding;
  private SpeciesViewModel speciesViewModel;
  private UserViewModel userViewModel;
  private User currentUser;
  private Species species;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentSpeciesCaseBinding.inflate(inflater, container, false);
    binding.addSpecies.setOnClickListener((v) -> Navigation
        .findNavController(binding.getRoot())
        .navigate(MainFragmentDirections.openSpeciesDialog())
    );
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentActivity activity = getActivity();
    LifecycleOwner owner = getViewLifecycleOwner();
    ViewModelProvider provider = new ViewModelProvider(activity);
    userViewModel = provider.get(UserViewModel.class);
    userViewModel
        .getCurrentUser()
        .observe(owner, (user) -> {
          currentUser = user;
          configureEditButton();
        });
    speciesViewModel = new ViewModelProvider(activity).get(SpeciesViewModel.class);
    speciesViewModel
        .getSpecies()
        .observe(owner, (species) -> {
          this.species = species;
          configureEditButton();
          // TODO Populate view objects on the screen based on species.
          binding.placeholderSpecies.setText(species.getNumber());

        });
  }

  private void configureEditButton() {
    if (currentUser != null && species != null) {
      if (currentUser.getId().equals(species.getLeadResearcher().getId())) {
        binding.editSpecies.setVisibility(View.VISIBLE);
        binding.editSpecies.setOnClickListener((v) -> Navigation
            .findNavController(binding.getRoot())
            .navigate(MainFragmentDirections.openSpeciesDialog().setSpeciesId(species.getId())));
      } else {
        binding.editSpecies.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}