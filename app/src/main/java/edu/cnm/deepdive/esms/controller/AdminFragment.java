package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.esms.adapter.AdminAdapter;
import edu.cnm.deepdive.esms.databinding.FragmentAdminBinding;
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.viewmodel.UserViewModel;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AdminFragment extends Fragment {

  private FragmentAdminBinding binding;
  private UserViewModel viewModel;
  private List<User> users;
  private User currentUser;
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
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
    viewModel
        .getCurrentUser()
        .observe(owner, (user) -> {
          currentUser = user;
          displayUsers();
        });
    viewModel
        .getUsers()
        .observe(owner, (users) -> {
          this.users = users;
          displayUsers();
        });
    viewModel.fetchUsers();
  }

  private void displayUsers() {
    if (currentUser != null && users != null) {
      adapter = new AdminAdapter(getContext(), users, currentUser,
          (user, role, checked) -> {
            if (checked) {
              user.getRoles().add(role);
            } else {
              user.getRoles().remove(role);
            }
            viewModel.updateUserRoles(user);
          },
          (user, checked) -> {
            user.setActive(checked);
            viewModel.updateUserActive(user);
          }
      );
      binding.userRoles.setAdapter(adapter);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
