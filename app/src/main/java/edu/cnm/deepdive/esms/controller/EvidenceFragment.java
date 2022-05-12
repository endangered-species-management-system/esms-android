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
import edu.cnm.deepdive.esms.databinding.FragmentEvidenceBinding;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.viewmodel.EvidenceViewModel;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import edu.cnm.deepdive.esms.viewmodel.TeamViewModel;
import edu.cnm.deepdive.esms.viewmodel.UserViewModel;
import java.util.Collection;

public class EvidenceFragment extends Fragment {

  private FragmentEvidenceBinding binding;
  private UserViewModel userViewModel;
  private SpeciesViewModel speciesViewModel;
  private TeamViewModel teamViewModel;
  private EvidenceViewModel evidenceViewModel;
  private User currentUser;
  private SpeciesCase speciesCase;
  private Collection<User> team;
  private Collection<Evidence> evidences;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentEvidenceBinding.inflate(inflater, container, false);
    binding.addEvidence.setOnClickListener((v) -> Navigation
        .findNavController(binding.getRoot())
        .navigate(MainFragmentDirections.openEvidenceDialog(speciesCase.getId()))
    );
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
    setupEvidenceViewModel(provider, owner);
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  private void setupUserViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    userViewModel = provider.get(UserViewModel.class);
    userViewModel
        .getCurrentUser()
        .observe(owner, (user) -> {
          currentUser = user;
          configureAddButton();
          displayEvidences();
        });
  }

  private void setupSpeciesViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    speciesViewModel = provider.get(SpeciesViewModel.class);
    speciesViewModel
        .getSpecies()
        .observe(owner, (species) -> {
          binding.addEvidence.setVisibility(View.GONE);
          // TODO Set recyclerview adapter to null.
          this.speciesCase = species;
          teamViewModel.fetchTeam(species.getId());
          evidenceViewModel.fetchEvidences(species.getId());
        });
  }

  private void setupTeamViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    teamViewModel = provider.get(TeamViewModel.class);
    teamViewModel
        .getTeam()
        .observe(owner, (team) -> {
          this.team = team;
          configureAddButton();
        });
  }

  private void setupEvidenceViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    evidenceViewModel = provider.get(EvidenceViewModel.class);
    evidenceViewModel
        .getEvidences()
        .observe(owner, (evidences) -> {
          this.evidences = evidences;
          displayEvidences();
        });
  }

  private void configureAddButton() {
    if (currentUser != null && team != null) {
      binding.addEvidence.setVisibility(
          (currentUser.equals(speciesCase.getLeadResearcher()) || team.contains(currentUser))
              ? View.VISIBLE
              : View.GONE
      );
    }
  }

  private void displayEvidences() {
    if (currentUser != null && evidences != null) {
      // TODO Create the recyclerview adapter pasing the context, the deletable flag and the
      //  collection of evidences; attach adapter to the recyclerview.
    }
  }
}