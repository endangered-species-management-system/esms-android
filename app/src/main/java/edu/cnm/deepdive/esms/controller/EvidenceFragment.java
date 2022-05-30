package edu.cnm.deepdive.esms.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.esms.adapter.EvidenceAdapter;
import edu.cnm.deepdive.esms.controller.MainFragmentDirections.OpenUploadDialog;
import edu.cnm.deepdive.esms.databinding.FragmentEvidenceBinding;
import edu.cnm.deepdive.esms.databinding.ItemEvidenceBinding;
import edu.cnm.deepdive.esms.model.entity.Attachment;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.viewmodel.EvidenceViewModel;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import edu.cnm.deepdive.esms.viewmodel.TeamViewModel;
import edu.cnm.deepdive.esms.viewmodel.UserViewModel;
import java.util.Collection;

public class EvidenceFragment extends Fragment {

  private static final int PICK_RESOURCE_REQUEST = 1023;
  private FragmentEvidenceBinding binding;
  private UserViewModel userViewModel;
  private SpeciesViewModel speciesViewModel;
  private TeamViewModel teamViewModel;
  private EvidenceViewModel evidenceViewModel;
  private User currentUser;
  private SpeciesCase speciesCase;
  private Collection<User> team;
  private Collection<Evidence> evidences;
  private NavController navController;
  private ActivityResultLauncher<Intent> launcher;
  private ItemEvidenceBinding itemEvidenceBinding;
  private Evidence evidence;
  private Attachment attachment;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    launcher = registerForActivityResult(new StartActivityForResult(),
        result -> {
          if (result.getResultCode() == Activity.RESULT_OK) {
            OpenUploadDialog action = MainFragmentDirections.openUploadDialog(
                result.getData().getData()).setEvidenceId(evidence.getId());
            Navigation.findNavController(binding.getRoot()).navigate(action);
          }
        });
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentEvidenceBinding.inflate(inflater, container, false);
    binding.addEvidence.setOnClickListener((v) -> getNavController()
        .navigate(MainFragmentDirections.openEvidenceDialog())
    );
    itemEvidenceBinding = ItemEvidenceBinding.inflate(inflater, container, false);
    itemEvidenceBinding.addAttachment.setOnClickListener((v) -> pickResource());
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

  private NavController getNavController() {
    if (navController == null) {
      navController = Navigation.findNavController(binding.getRoot());
    }
    return navController;
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
      if (!currentUser.getRoles().isEmpty()) {
        boolean deletable = speciesCase.getLeadResearcher().equals(currentUser);
        EvidenceAdapter.OnRemoveClickListener onRemoveClickListener = deletable
            ? (evidence) -> evidenceViewModel.deleteEvidence(speciesCase.getId(), evidence)
            : (evidence) -> {
            };
        EvidenceAdapter.OnClickListener onClickListener = (evidence) ->
            getNavController().navigate(
                MainFragmentDirections.openEvidenceDialog().setEvidenceId(evidence.getId()));
        EvidenceAdapter.OnAttachClickListener onAttachClickListener = (evidence) -> {
          this.evidence = evidence;
          pickResource();
        };
        EvidenceAdapter.OnAttachmentItemClickListener onAttachmentItemClickListener = (attachment)->
            getNavController().navigate(
                MainFragmentDirections.openAttachmentDialog().setAttachmentId(attachment.getId()));
        EvidenceAdapter adapter = new EvidenceAdapter(getContext(), evidences, deletable,
            onClickListener, onRemoveClickListener, onAttachClickListener, onAttachmentItemClickListener);
        binding.evidencesRecyclerview.setAdapter(adapter);
      }
    }
  }

  private void pickResource() {
    Intent intent = new Intent();
    intent.setType("*/*");
    intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "text/*", "application/*"});
    intent.setAction(Intent.ACTION_GET_CONTENT);
    launcher.launch(intent);
  }

}