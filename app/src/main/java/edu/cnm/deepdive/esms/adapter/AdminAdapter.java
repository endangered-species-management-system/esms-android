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

  public AdminAdapter(Context context, List<User> users) {
    inflater = LayoutInflater.from(context);
    this.users = users;
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
      binding.adminRoleBox.setChecked(user.getRoles().contains("ADMINISTRATOR"));
      binding.leadRoleBox.setChecked(user.getRoles().contains("LEAD"));
      binding.researcherRoleBox.setChecked(user.getRoles().contains("RESEARCHER"));
      binding.activeBox.setChecked(user.isActive());
    }
  }

}
