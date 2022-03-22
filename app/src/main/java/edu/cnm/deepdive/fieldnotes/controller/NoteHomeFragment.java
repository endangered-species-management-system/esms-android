package edu.cnm.deepdive.fieldnotes.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.fieldnotes.NavigationDirections;
import edu.cnm.deepdive.fieldnotes.R;
import edu.cnm.deepdive.fieldnotes.adapter.NoteAdapter;
import edu.cnm.deepdive.fieldnotes.databinding.FragmentNoteHomeBinding;
import edu.cnm.deepdive.fieldnotes.model.entity.Species;
import edu.cnm.deepdive.fieldnotes.viewmodel.MainViewModel;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class NoteHomeFragment extends Fragment {

  private static final int PICK_IMAGE_REQUEST = 1023;
  private FragmentNoteHomeBinding binding;
  private List<Species> speciesList;
  private MainViewModel mainViewModel;
  private NoteAdapter noteAdapter;
  private long speciesId;
  private long speciesIdFromSpinner;

  public static NoteHomeFragment newInstance() {
    return new NoteHomeFragment();
  }

  @Override
  public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      NoteDialogFragmentArgs args = NoteDialogFragmentArgs.fromBundle(getArguments());
      speciesId = args.getSpeciesId();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode,
      @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
      @NonNull NavigationDirections.OpenNoteDialog action = NavigationDirections.openNoteDialog(speciesId,data.getData());
      Navigation.findNavController(binding.getRoot()).navigate(action);
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    binding = FragmentNoteHomeBinding.inflate(inflater, container, false);
    binding.speciesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Species species = (Species) adapterView.getItemAtPosition(position);
        mainViewModel.setSpeciesId(species.getId());
        binding.addNote.setOnClickListener((value) -> {
          Log.i(getClass().getSimpleName(), value.toString());
          speciesId = ((Species) binding.speciesSpinner.getSelectedItem()).getId();
          pickImage();
        });
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // TODO Need to replace recent notes display with notes tied to specific species

    mainViewModel.getSpeciesNotes()
        .observe(getViewLifecycleOwner(), (notes) -> {
          if (notes != null) {
            binding.recentNotes.setAdapter(new NoteAdapter(getContext(), (notes)));
          }
        });

    mainViewModel.loadSpecies().observe(getViewLifecycleOwner(), (list) -> {
      this.speciesList = list;
      ArrayAdapter<Species> adapter = new ArrayAdapter<>(getContext(),
          R.layout.support_simple_spinner_dropdown_item, speciesList);
      binding.speciesSpinner.setAdapter(adapter);
    });
  }

  private void pickImage() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Choose image to upload"),
        PICK_IMAGE_REQUEST);
  }
}
