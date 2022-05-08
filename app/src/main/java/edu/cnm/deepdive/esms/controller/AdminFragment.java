package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;
import edu.cnm.deepdive.esms.R;
import edu.cnm.deepdive.esms.adapter.AdminAdapter;
import edu.cnm.deepdive.esms.databinding.FragmentAdminBinding;
import edu.cnm.deepdive.esms.databinding.FragmentCaseBinding;
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.viewmodel.UserViewModel;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AdminFragment extends Fragment {

  private FragmentAdminBinding binding;
  private UserViewModel viewModel;
  private List<User> users;
  private AdminAdapter adapter;

  @NotNull
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentAdminBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(this).get(UserViewModel.class);
    viewModel
        .getAll()
        .observe(getViewLifecycleOwner(), (users) -> {
          this.users = users;
          adapter = new AdminAdapter(getContext(),users);
          binding.userRoles.setAdapter(adapter);
        });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
