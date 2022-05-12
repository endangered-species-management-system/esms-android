package edu.cnm.deepdive.esms.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.esms.R;
import edu.cnm.deepdive.esms.databinding.FragmentSpeciesDialogBinding;
import edu.cnm.deepdive.esms.model.entity.Phase;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import java.util.UUID;

public class SpeciesDialogFragment extends DialogFragment implements TextWatcher {

  private SpeciesViewModel viewModel;
  private FragmentSpeciesDialogBinding binding;
  private AlertDialog alertDialog;
  private UUID speciesID;
  private SpeciesCase speciesCase;
  private ArrayAdapter<Phase> adapter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      SpeciesDialogFragmentArgs args = SpeciesDialogFragmentArgs.fromBundle(getArguments());
      speciesID = args.getSpeciesId();
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(
      @Nullable Bundle savedInstanceState) {
    binding = FragmentSpeciesDialogBinding.inflate(LayoutInflater.from(getContext()));
    //noinspection ConstantConditions
    alertDialog = new Builder(getContext())
        .setTitle(R.string.new_species)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.cancel, (dlg, which) -> {
        })
        .setPositiveButton(android.R.string.ok, (dlg, which) -> saveSpecies())
        .create();
    alertDialog.setOnShowListener((dlg) -> {
      binding.name.addTextChangedListener(this);
      binding.summary.addTextChangedListener(this);
//      binding.name.addTextChangedListener(this);
      checkSubmitConditions();
    });
    return alertDialog;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    adapter = new ArrayAdapter<>(getContext(),
        android.R.layout.simple_spinner_item, Phase.values());
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    binding.phase.setAdapter(adapter);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(getActivity()).get(SpeciesViewModel.class);
    if (speciesID != null) {
      viewModel
          .getSpecies()
          .observe(getViewLifecycleOwner(), (species) -> {
            if (species.getId().equals(speciesID)) {
              this.speciesCase = species;
              binding.name.setText(species.getName());
              binding.summary.setText(species.getSummary());
              binding.phase.setSelection(adapter.getPosition(species.getPhase()));
              // TODO Populate the view objects in binding with the properties of species and display them.
            } else {
              viewModel.fetchSpecies(speciesID);
            }
          });
    } else {
      speciesCase = new SpeciesCase();
    }
  }

  private void saveSpecies() {
    speciesCase.setName(binding.name.getText().toString().trim());
    speciesCase.setSummary(binding.summary.getText().toString());
    speciesCase.setPhase((Phase) binding.phase.getSelectedItem());
    // TODO Populate additional content as needed.
    viewModel.saveSpecies(speciesCase);
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    // Do nothing
  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    // Do nothing
  }

  @Override
  public void afterTextChanged(Editable editable) {
    checkSubmitConditions();
  }

  private void checkSubmitConditions() {
    alertDialog
        .getButton(DialogInterface.BUTTON_POSITIVE)
        .setEnabled(
            !binding.name.getText().toString().trim().isEmpty()
                && !binding.summary.getText().toString().trim().isEmpty()
        ); // TODO Look at all necessary fields
  }


}