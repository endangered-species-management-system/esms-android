package edu.cnm.deepdive.fieldnotes.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.fieldnotes.NavigationDirections.OpenNote;
import edu.cnm.deepdive.fieldnotes.R;
import edu.cnm.deepdive.fieldnotes.databinding.FragmentNoteHomeBinding;

public class NoteHomeFragment extends Fragment {

  FragmentNoteHomeBinding binding;

  public static NoteHomeFragment newInstance() {
    return new NoteHomeFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentNoteHomeBinding.inflate(inflater, container, false);
    binding.addNote.setOnClickListener( (view) ->
        Navigation.findNavController(binding.getRoot()).navigate(R.id.note_dialog));

    return binding.getRoot();
  }


}