package edu.cnm.deepdive.fieldnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.fieldnotes.adapter.NoteAdapter.Holder;
import edu.cnm.deepdive.fieldnotes.databinding.ItemNoteBinding;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import edu.cnm.deepdive.fieldnotes.model.entity.Note.NoteType;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class NoteAdapter extends RecyclerView.Adapter<Holder> {

  private final List<Note> notes;
  private Context context;
  private final LayoutInflater inflater;

  public NoteAdapter(Context context, List<Note> notes) {
    this.notes = notes;
    this.context = context;
    inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
    ItemNoteBinding binding = ItemNoteBinding.inflate(inflater, parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return notes.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    private final ItemNoteBinding binding;

    public Holder(@NonNull ItemNoteBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
      binding.getRoot();
    }

    private void bind(int position) {
      Note note = notes.get(position);
      NoteType type = note.getType();
      binding.note.setText(note.getNote());
      if(type != null) {
      binding.type.setText(type.toString());
      }
      binding.getRoot();
    }
  }
}
