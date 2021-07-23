package edu.cnm.deepdive.fieldnotes.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.fieldnotes.R;
import edu.cnm.deepdive.fieldnotes.databinding.FragmentNoteHomeBinding;
import edu.cnm.deepdive.fieldnotes.model.entity.Species;
import edu.cnm.deepdive.fieldnotes.viewmodel.MainViewModel;
import java.util.List;

public class NoteHomeFragment extends Fragment {

  FragmentNoteHomeBinding binding;
  private List<Species> speciesList;

  public static NoteHomeFragment newInstance() {
    return new NoteHomeFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentNoteHomeBinding.inflate(inflater, container, false);
    MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    mainViewModel.loadSpecies().observe(getViewLifecycleOwner(), (list) -> {
      this.speciesList = list;
      ArrayAdapter<Species> adapter = new ArrayAdapter<>(getContext(),
          R.layout.support_simple_spinner_dropdown_item, speciesList);
      binding.speciesSpinner.setAdapter(adapter);
    });
    binding.addNote.setOnClickListener((view) ->
        Navigation.findNavController(binding.getRoot()).navigate(R.id.note_dialog));

    return binding.getRoot();
  }


}