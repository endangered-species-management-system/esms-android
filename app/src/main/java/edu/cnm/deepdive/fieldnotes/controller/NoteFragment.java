package edu.cnm.deepdive.fieldnotes.controller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.fieldnotes.R;
import edu.cnm.deepdive.fieldnotes.databinding.FragmentNoteBinding;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import edu.cnm.deepdive.fieldnotes.model.entity.Note.NoteType;
import edu.cnm.deepdive.fieldnotes.viewmodel.MainViewModel;
import java.util.List;

public class NoteFragment extends DialogFragment implements TextWatcher, OnClickListener {

  private MainViewModel viewModel;
  private FragmentNoteBinding binding;
  private AlertDialog alertDialog;
  private Long speciesId;
  private NoteType type;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      NoteFragmentArgs args = NoteFragmentArgs.fromBundle(getArguments());
      speciesId = args.getSpeciesId();
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = FragmentNoteBinding.inflate(LayoutInflater.from(getContext()));
    //noinspection ConstantConditions
    alertDialog = new AlertDialog.Builder(getContext())
        .setTitle(R.string.new_note)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.cancel, (dlg, which) -> {
        })
        .setPositiveButton(android.R.string.ok, (dlg, which) -> saveNote())
        .create();
    alertDialog.setOnShowListener((dlg) -> {
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
    //noinspection ConstantConditions
    viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    viewModel.getNote().observe(getViewLifecycleOwner(), (notes) -> {
    });
    setNoteType();
  }

  private void setNoteType() {
    binding.radioButtonLocation.setOnClickListener(this);
    binding.radioButtonSeason.setOnClickListener(this);
    binding.radioButtonConditions.setOnClickListener(this);
  }

  private void checkSubmitConditions() {
    Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
    positive.setEnabled(!binding.note.getText().toString().trim().isEmpty());
  }

  private void saveNote() {
    Note note = new Note();
    String newNote = binding.note.getText().toString().trim();
    note.setNote(newNote);
    note.setSpeciesId(speciesId);
    note.setType(type);
    viewModel.saveNote(note);
  }

  @Override
  public void onClick(View view) {
    boolean checked = ((RadioButton) view).isChecked();
    switch (view.getId()) {
      case R.id.radio_button_season:
        if (checked) {
          type = NoteType.SEASON;
        }
        break;
      case R.id.radio_button_location:
        if (checked) {
          type = NoteType.LOCATION;
        }
        break;
      case R.id.radio_button_conditions:
        if (checked) {
          type = NoteType.CONDITIONS;
        }
        break;
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
}