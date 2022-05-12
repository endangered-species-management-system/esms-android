package edu.cnm.deepdive.esms.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EvidenceDialogFragment extends DialogFragment implements TextWatcher, OnClickListener {

  private EvidenceViewModel evidenceViewModel;
  private Evidence note;
  private UUID evidenceId;
  private User researcher;
  private User lead;
  private UUID speciesId;
  private SpeciesCase speciesCase;
  FragmentEvidenceDialogBinding binding;
  private AlertDialog alertDialog;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      SpeciesDialogFragmentArgs args = SpeciesDialogFragmentArgs.fromBundle(getArguments());
      speciesId = args.getSpeciesId();
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = FragmentEvidenceDialogBinding.inflate(LayoutInflater.from(getContext()));

    alertDialog = new Builder(getContext())
        .setTitle(R.string.new_evidence_note)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.cancel, (dlg, which) -> {
        })
        .setPositiveButton(android.R.string.ok, (dlg, which) -> saveNote())
        .create();
    alertDialog.setOnShowListener((dlg) -> {
      binding.note.addTextChangedListener(this);
      // TODO Add name and location
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
    evidenceViewModel = new ViewModelProvider(getActivity()).get(EvidenceViewModel.class);
    if (speciesId != null) {
      evidenceViewModel
          .getEvidence()
          .observe(getViewLifecycleOwner(), (ev) -> {
            if (ev.getId().equals(evidenceId)) {
              this.note = ev;
              binding.note.setText(ev.getNote());
              // TODO Populate the view objects in binding with the properties of species and display them.
            } else {
              evidenceViewModel.fetchEvidence(ev.getSpeciesCase().getId(), evidenceId);
            }
          });
    } else {
      note = new Evidence();
    }
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

  @Override
  public void onClick(View view) {

  }

  private void checkSubmitConditions() {
    alertDialog
        .getButton(DialogInterface.BUTTON_POSITIVE)
        .setEnabled(
            !binding.note.getText().toString().trim().isEmpty()
        ); // TODO Look at all necessary fields
  }

  private void saveNote() {
    note = new Evidence();

  }
}
