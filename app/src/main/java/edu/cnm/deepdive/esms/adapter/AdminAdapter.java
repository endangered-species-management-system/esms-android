package edu.cnm.deepdive.esms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.esms.adapter.AdminAdapter.Holder;
import edu.cnm.deepdive.esms.databinding.ItemUserRolesBinding;
import edu.cnm.deepdive.esms.model.entity.User;
import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<Holder> {

  private final LayoutInflater inflater;
  private final List<User> users;
  private final User currentUser;
  private final OnRoleToggleListener onRoleToggleListener;
  private final OnActiveToggleListener onActiveToggleListener;

  public AdminAdapter(Context context, List<User> users, User currentUser, OnRoleToggleListener onRoleToggleListener,
      OnActiveToggleListener onActiveToggleListener) {
    inflater = LayoutInflater.from(context);
    this.users = users;
    this.currentUser = currentUser;
    this.onRoleToggleListener = onRoleToggleListener;
    this.onActiveToggleListener = onActiveToggleListener;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemUserRolesBinding binding = ItemUserRolesBinding.inflate(inflater, parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    private final ItemUserRolesBinding binding;

    public Holder(@NonNull ItemUserRolesBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      User user = users.get(position);
      binding.displayName.setText(user.getDisplayName());
      boolean admin = user.getRoles().contains("ADMINISTRATOR");
      binding.adminRoleBox.setChecked(admin);
      binding.adminRoleBox.setEnabled(!user.getId().equals(currentUser.getId()));
      binding.leadRoleBox.setChecked(user.getRoles().contains("LEAD"));
      binding.researcherRoleBox.setChecked(user.getRoles().contains("RESEARCHER"));
      binding.activeBox.setChecked(user.isActive());
      binding.activeBox.setEnabled(!user.getId().equals(currentUser.getId()));
      binding.adminRoleBox.setOnCheckedChangeListener((compoundButton, checked) ->
          onRoleToggleListener.onRoleToggle(user, "ADMINISTRATOR", checked));
      binding.leadRoleBox.setOnCheckedChangeListener((compoundButton, checked) ->
          onRoleToggleListener.onRoleToggle(user, "LEAD", checked));
      binding.researcherRoleBox.setOnCheckedChangeListener((compoundButton, checked) ->
          onRoleToggleListener.onRoleToggle(user, "RESEARCHER", checked));
      binding.activeBox.setOnCheckedChangeListener((compoundButton, checked) ->
          onActiveToggleListener.onActiveToggle(user, checked));
    }
  }

  public interface OnRoleToggleListener {

    void onRoleToggle(User user, String role, boolean checked);
  }

  public interface OnActiveToggleListener {

    void onActiveToggle(User user, boolean checked);
  }
}
