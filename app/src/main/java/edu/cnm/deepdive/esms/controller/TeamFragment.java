package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.esms.adapter.TeamAdapter;
import edu.cnm.deepdive.esms.databinding.FragmentSpeciesCaseBinding;
import edu.cnm.deepdive.esms.databinding.FragmentTeamBinding;
import edu.cnm.deepdive.esms.model.entity.Species;
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import edu.cnm.deepdive.esms.viewmodel.UserViewModel;
import java.util.Collection;

public class TeamFragment extends Fragment {

  private FragmentTeamBinding binding;
  private UserViewModel userViewModel;
  private SpeciesViewModel speciesViewModel;
  private User currentUser;
  private Species species;
  private Collection<User> team;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentTeamBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(getActivity());
    LifecycleOwner owner = getViewLifecycleOwner();
    userViewModel = provider.get(UserViewModel.class);
    userViewModel
        .getCurrentUser()
        .observe(owner, (user) -> {
          currentUser = user;
          configureTeamControls();
        });
    speciesViewModel = provider.get(SpeciesViewModel.class);
    speciesViewModel
        .getSpecies()
        .observe(owner, (speciesCase) -> {
          species = speciesCase;
          binding.addTeamMember.setVisibility(View.GONE);
          binding.members.setAdapter(null);
          speciesViewModel.fetchTeam(species.getId());
          configureTeamControls();
        });
    speciesViewModel
        .getTeam()
        .observe(owner, (team) -> {
          this.team = team;
          displayTeam();
        });

  }

  private void displayTeam() {
    if (currentUser != null && team != null) {
      if (currentUser.getId().equals(species.getLeadResearcher().getId())) {
        TeamAdapter adapter = new TeamAdapter(getContext(), team,
            species.getLeadResearcher().equals(currentUser), (user) -> {
          speciesViewModel.setTeamMember(species, user, false);
        });
        binding.members.setAdapter(adapter);
      }
    }
  }

  private void configureTeamControls() {
    if (currentUser != null && species != null) {
      boolean teamControlsEnabled = currentUser.getId().equals(species.getLeadResearcher().getId());
      if (teamControlsEnabled) {
        binding.addTeamMember.setVisibility(View.VISIBLE);
        binding.addTeamMember.setOnClickListener((v) -> Navigation
            .findNavController(binding.getRoot())
            .navigate(MainFragmentDirections.openTeamDialog(species.getId()))
        );
      }

      // TODO HIde or show the various controls based on teamControlsEnabled.
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}