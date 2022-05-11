package edu.cnm.deepdive.esms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.esms.adapter.TeamAdapter.Holder;
import edu.cnm.deepdive.esms.databinding.ItemTeamBinding;
import edu.cnm.deepdive.esms.model.entity.User;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<Holder> {

  private final LayoutInflater inflater;
  private final List<User> users;
  private final User currentUser;

  public TeamAdapter(Context context, List<User> users, User currentUser) {
    inflater = LayoutInflater.from(context);
    this.users = users;
    this.currentUser = currentUser;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemTeamBinding binding = ItemTeamBinding.inflate(inflater, parent, false);
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

    private final ItemTeamBinding binding;

    public Holder(@NonNull ItemTeamBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      User member = users.get(position);
      binding.member.setText(member.getDisplayName());
    }
  }

}
