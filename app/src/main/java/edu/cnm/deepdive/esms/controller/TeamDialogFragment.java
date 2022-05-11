package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import edu.cnm.deepdive.esms.R;
import edu.cnm.deepdive.esms.databinding.FragmentSpeciesDialogBinding;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import edu.cnm.deepdive.esms.viewmodel.UserViewModel;

public class TeamDialogFragment extends BottomSheetDialogFragment {

  private FragmentSpeciesDialogBinding binding;
  private UserViewModel userViewModel;
  private SpeciesViewModel speciesViewModel;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_team_dialog, container, false);
  }
}