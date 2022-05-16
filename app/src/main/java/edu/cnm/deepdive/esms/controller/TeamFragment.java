package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.esms.adapter.TeamAdapter;
import edu.cnm.deepdive.esms.databinding.FragmentTeamBinding;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import edu.cnm.deepdive.esms.viewmodel.TeamViewModel;
import edu.cnm.deepdive.esms.viewmodel.UserViewModel;
import java.util.Collection;

public class TeamFragment extends Fragment {

  private FragmentTeamBinding binding;
  private UserViewModel userViewModel;
  private SpeciesViewModel speciesViewModel;
  private TeamViewModel teamViewModel;
  private User currentUser;
  private SpeciesCase speciesCase;
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
    setupUserViewModel(provider, owner);
    setupSpeciesViewModel(provider, owner);
    setupTeamViewModel(provider, owner);

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  private void setupUserViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    userViewModel = provider.get(UserViewModel.class);
    userViewModel
        .getCurrentUser()
        .observe(owner, (user) -> {
          currentUser = user;
          configureTeamControls();
        });
  }

  private void setupSpeciesViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    speciesViewModel = provider.get(SpeciesViewModel.class);
    speciesViewModel
        .getSpecies()
        .observe(owner, (speciesCase) -> {
          this.speciesCase = speciesCase;
          binding.addTeamMember.setVisibility(View.GONE);
          binding.members.setAdapter(null);
          teamViewModel.fetchTeam(this.speciesCase.getId());
          configureTeamControls();
        });
  }

  private void setupTeamViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    teamViewModel = provider.get(TeamViewModel.class);
    teamViewModel
        .getTeam()
        .observe(owner, (team) -> {
          this.team = team;
          displayTeam();
        });
  }

  private void configureTeamControls() {
    if (currentUser != null && speciesCase != null) {
      boolean teamControlsEnabled = currentUser.getId()
          .equals(speciesCase.getLeadResearcher().getId());
      if (teamControlsEnabled) {
        binding.addTeamMember.setVisibility(View.VISIBLE);
        binding.addTeamMember.setOnClickListener((v) -> Navigation
            .findNavController(binding.getRoot())
            .navigate(MainFragmentDirections.openTeamDialog(speciesCase.getId()))
        );
      }
    }
  }

  private void displayTeam() {
    if (currentUser != null && team != null) {
      if (currentUser.getId().equals(speciesCase.getLeadResearcher().getId())) {
        boolean deletable = speciesCase.getLeadResearcher().equals(currentUser);
        TeamAdapter.OnRemoveClickListener listener = deletable
            ? (user) -> teamViewModel.setTeamMember(speciesCase.getId(), user, false)
            : (user) -> {};
        TeamAdapter adapter = new TeamAdapter(getContext(), team, deletable, listener);
        binding.members.setAdapter(adapter);
      }
    }
  }

}