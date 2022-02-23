package edu.cnm.deepdive.fieldnotes.controller;

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
import edu.cnm.deepdive.fieldnotes.NavigationDirections.OpenNote;
import edu.cnm.deepdive.fieldnotes.R;
import edu.cnm.deepdive.fieldnotes.adapter.NoteAdapter;
import edu.cnm.deepdive.fieldnotes.databinding.FragmentNoteHomeBinding;
import edu.cnm.deepdive.fieldnotes.model.entity.Species;
import edu.cnm.deepdive.fieldnotes.viewmodel.MainViewModel;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class NoteHomeFragment extends Fragment {

  private FragmentNoteHomeBinding binding;
  private List<Species> speciesList;
  private MainViewModel mainViewModel;
  private NoteAdapter noteAdapter;
  private long speciesId;

  public static NoteHomeFragment newInstance() {
    return new NoteHomeFragment();
  }

  @Override
  public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      NoteFragmentArgs args = NoteFragmentArgs.fromBundle(getArguments());
      speciesId = args.getSpeciesId();
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    binding = FragmentNoteHomeBinding.inflate(inflater, container, false);
    binding.addNote.setOnClickListener((value) -> {
      long speciesId = ((Species) binding.speciesSpinner.getSelectedItem()).getId();
      @NonNull OpenNote action = NavigationDirections.openNote(speciesId);
      Navigation.findNavController(binding.getRoot()).navigate(action);
    });
    binding.speciesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Species species = (Species) adapterView.getItemAtPosition(position);
        mainViewModel.setSpeciesId(species.getId());
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view,
      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // TODO Need to replace recent notes display with notes tied to specific species
    mainViewModel.getNotesBySpecies(speciesId).observe(getViewLifecycleOwner(), (notes) -> {
      if (notes != null) {
        binding.recentNotes.setAdapter(new NoteAdapter(getContext(), notes));
        Log.i(getClass().getSimpleName(), "IS SPECIES ID COMING THROUGH? " + speciesId);
      }
    });

    mainViewModel.loadSpecies().observe(getViewLifecycleOwner(), (list) -> {
      this.speciesList = list;
      ArrayAdapter<Species> adapter = new ArrayAdapter<>(getContext(),
          R.layout.support_simple_spinner_dropdown_item, speciesList);
      binding.speciesSpinner.setAdapter(adapter);
    });
  }
}
