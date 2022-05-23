package edu.cnm.deepdive.esms.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.esms.R;
import edu.cnm.deepdive.esms.databinding.FragmentEvidenceDialogBinding;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.viewmodel.EvidenceViewModel;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import edu.cnm.deepdive.esms.viewmodel.UserViewModel;
import java.util.UUID;

public class EvidenceDialogFragment extends DialogFragment implements TextWatcher {

  private EvidenceViewModel evidenceViewModel;
  private UserViewModel userViewModel;
  private SpeciesViewModel speciesViewModel;
  private Evidence evidence;
  private UUID evidenceId;
  private User currentUser;
  private SpeciesCase speciesCase;
  FragmentEvidenceDialogBinding binding;
  private AlertDialog alertDialog;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      EvidenceDialogFragmentArgs args = EvidenceDialogFragmentArgs.fromBundle(getArguments());
      evidenceId = args.getEvidenceId();
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = FragmentEvidenceDialogBinding.inflate(LayoutInflater.from(getContext()));

    alertDialog = new Builder(getContext())
        .setTitle(R.string.new_evidence_title)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.cancel, (dlg, which) -> {
        })
        .setPositiveButton(android.R.string.ok, (dlg, which) -> saveEvidence())
        .create();
    alertDialog.setOnShowListener((dlg) -> {
      binding.name.addTextChangedListener(this);
      binding.note.addTextChangedListener(this);
      checkSubmitConditions();
    });
    return alertDialog;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(getActivity());
    LifecycleOwner owner = getViewLifecycleOwner();
    setupUserViewModel(provider, owner);
    setupSpeciesViewModel(provider, owner);
    setupEvidenceViewModel(provider, owner);
    // TODO Connect to viewmodel for attachments
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void afterTextChanged(Editable editable) {
    checkSubmitConditions();
  }

  private void checkSubmitConditions() {
    alertDialog
        .getButton(DialogInterface.BUTTON_POSITIVE)
        .setEnabled(
            speciesCase != null
                && !binding.name.getText().toString().trim().isEmpty()
                && !binding.note.getText().toString().trim().isEmpty()
        );
  }

  private void saveEvidence() {
    evidence.setName(binding.name.getText().toString().trim());
    evidence.setNote(binding.note.getText().toString().trim());
    String location = binding.location.getText().toString().trim();
    evidence.setLocation(location.isEmpty() ? null : location);
    if (evidence.getId() == null) {
      evidenceViewModel.addEvidence(speciesCase.getId(), evidence);
    }
    // TODO Save attachments
    evidenceViewModel
        .loadAttachments(speciesCase.getId(),evidenceId);
  }

  private void setupUserViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    userViewModel = provider.get(UserViewModel.class);
    userViewModel
        .getCurrentUser()
        .observe(owner, (user) -> {
          currentUser = user;
          // TODO Configure controls for adding attachments
        });
  }

  private void setupSpeciesViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    speciesViewModel = provider.get(SpeciesViewModel.class);
    speciesViewModel
        .getSpecies()
        .observe(owner, (species) -> {
          speciesCase = species;
          fetchEvidence();
        });
  }

  private void setupEvidenceViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    evidenceViewModel = provider.get(EvidenceViewModel.class);
    if (evidenceId != null) {
      evidenceViewModel
          .getEvidence()
          .observe(owner, (ev) -> {
            this.evidence = ev;
            binding.name.setEnabled(false);
            binding.name.setText(ev.getName());
            binding.note.setEnabled(
                false); // Remove this line if lead researcher is allowed to edit this.
            binding.note.setText(ev.getNote());
            binding.location.setEnabled(
                false); // Remove this line if lead researcher is allowed to edit this.
            binding.location.setText(ev.getLocation());
          });
      fetchEvidence();
    } else {
      evidence = new Evidence();
    }
  }

  private void fetchEvidence() {
    if (speciesCase != null && evidenceId != null) {
      evidenceViewModel.fetchEvidence(speciesCase.getId(), evidenceId);
    }
  }

}
