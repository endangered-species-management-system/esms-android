package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import edu.cnm.deepdive.esms.databinding.FragmentTeamDialogBinding;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import edu.cnm.deepdive.esms.viewmodel.UserViewModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TeamDialogFragment extends BottomSheetDialogFragment implements
    OnItemSelectedListener {

  private FragmentTeamDialogBinding binding;
  private UserViewModel userViewModel;
  private SpeciesViewModel speciesViewModel;
  private User currentUser;
  private SpeciesCase speciesCase;
  private User lead;
  private Set<User> team;
  private List<User> users;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentTeamDialogBinding.inflate(inflater, container, false);
    binding.membersSelector.setOnItemSelectedListener(this);
    binding.save.setOnClickListener((v) -> {
      User user = (User) binding.membersSelector.getSelectedItem();
      speciesViewModel.setTeamMember(speciesCase, user, true);
      dismiss();
    });
    binding.cancel.setOnClickListener((v) -> dismiss());
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(getActivity());
    LifecycleOwner owner = getViewLifecycleOwner();
    userViewModel = provider.get(UserViewModel.class);
    userViewModel
        .getUsers()
        .observe(owner, (users) -> {
          this.users = users;
          populateSpinner();
        });
    userViewModel.fetchUsers("RESEARCHER");
    userViewModel
        .getCurrentUser()
        .observe(owner, (user) -> {
          currentUser = user;
        });
    speciesViewModel = provider.get(SpeciesViewModel.class);
    speciesViewModel
        .getSpecies()
        .observe(owner, (speciesCase) -> {
          this.speciesCase = speciesCase;
          lead = speciesCase.getLeadResearcher();
          populateSpinner();
        });
    speciesViewModel
        .getTeam()
        .observe(owner, (team) -> {
          this.team = new HashSet<>(team);
          populateSpinner();
        });
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  private void populateSpinner() {
    if (lead != null && team != null && users != null) {
      List<User> content = users
          .stream()
          .filter((u) -> !u.equals(lead))
          .filter((u) -> !team.contains(u))
          .sorted()
          .collect(Collectors.toList());
      ArrayAdapter<User> adapter = new ArrayAdapter<>(getContext(),
          android.R.layout.simple_spinner_item, content);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      binding.membersSelector.setAdapter(adapter);
    }
  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    binding.save.setEnabled(true);
    binding.save.setVisibility(View.VISIBLE);
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {
    binding.save.setEnabled(false);
    binding.save.setVisibility(View.INVISIBLE);
  }
}