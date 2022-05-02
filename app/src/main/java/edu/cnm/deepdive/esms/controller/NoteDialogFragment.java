package edu.cnm.deepdive.esms.controller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.esms.R;
import edu.cnm.deepdive.esms.databinding.FragmentDialogNoteBinding;
import edu.cnm.deepdive.esms.model.entity.Note;
import edu.cnm.deepdive.esms.model.entity.Note.NoteType;
import edu.cnm.deepdive.esms.viewmodel.MainViewModel;

public class NoteDialogFragment extends DialogFragment implements TextWatcher, OnClickListener {

  private MainViewModel viewModel;
  private FragmentDialogNoteBinding binding;
  private Uri uri;
  private AlertDialog alertDialog;
  private Long speciesId;
  private NoteType type;
//  private List<Case> cases;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      NoteDialogFragmentArgs args = NoteDialogFragmentArgs.fromBundle(getArguments());
      speciesId = args.getSpeciesId();
    }
    uri = NoteDialogFragmentArgs.fromBundle(getArguments()).getContentUri();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = FragmentDialogNoteBinding.inflate(LayoutInflater.from(getContext()));
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
    Picasso
        .get()
        .load(uri)
        .into(binding.image);
    //noinspection ConstantConditions
    viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
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


  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View view) {
    boolean checked = ((RadioButton) view).isChecked();
    switch (view.getId()) {
      case R.id.radio_button_season:
        if (checked) {
          type = NoteType.ATTRIBUTE;
        }
        break;
      case R.id.radio_button_location:
        if (checked) {
          type = NoteType.HABITAT;
        }
        break;
      case R.id.radio_button_conditions:
        if (checked) {
          type = NoteType.GENERAL;
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

  private void saveNote() {
    Note note = new Note();
    String newNote = binding.note.getText().toString().trim();
    note.setNote(newNote);
    note.setSpeciesId(speciesId);
    note.setType(type);
    if (note.getHref() != null) {
      note.setHref(String.valueOf(uri));
    } else {
      note.setHref("No image");
    }
    note.setImageName("?");
    note.setContentType("png");
    viewModel.saveNote(note);
  }

/*  @SuppressWarnings("ConstantConditions")
  private void upload() {
    String title = binding.title.getText().toString().trim();
    String description = binding.galleryDescription.getText().toString().trim();
    String galleryTitle = binding.galleryTitle.getText().toString().trim();
    UUID galleryId = null;
    for (Gallery g : galleries) {
      if (g != null && galleryTitle.equals(g.getTitle())) {
        galleryId = g.getId();
      }
    }
    imageViewModel.store(galleryId, uri, title, (description.isEmpty() ? null : description));
  }*/
}