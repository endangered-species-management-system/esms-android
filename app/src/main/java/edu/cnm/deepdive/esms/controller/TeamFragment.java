package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.esms.databinding.FragmentSpeciesCaseBinding;
import edu.cnm.deepdive.esms.databinding.FragmentTeamBinding;

public class TeamFragment extends Fragment {

  private FragmentTeamBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentTeamBinding.inflate(inflater, container, false);
    binding.addTeamMember.setOnClickListener((v) -> Navigation
        .findNavController(binding.getRoot())
        .navigate(MainFragmentDirections.openTeamDialog())
    );
    return binding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}