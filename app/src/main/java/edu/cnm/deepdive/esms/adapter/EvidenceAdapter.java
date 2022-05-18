package edu.cnm.deepdive.esms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.esms.adapter.EvidenceAdapter.Holder;
import edu.cnm.deepdive.esms.databinding.ItemEvidenceBinding;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import java.text.DateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class EvidenceAdapter extends RecyclerView.Adapter<Holder> {

  private final LayoutInflater inflater;
  private final List<Evidence> evidences;
  private final boolean lead;
  private final OnClickListener onClickListener;
  private final OnRemoveClickListener onRemoveClickListener;
  private final OnAttachClickListener onAttachClickListener;
  private final DateFormat dateFormat;

  public EvidenceAdapter(Context context, Collection<Evidence> evidences, boolean lead,
      OnClickListener onClickListener, OnRemoveClickListener onRemoveClickListener,
      OnAttachClickListener onAttachClickListener) {
    inflater = LayoutInflater.from(context);
    this.evidences = new LinkedList<>(evidences);
    this.lead = lead;
    this.onClickListener = onClickListener;
    this.onRemoveClickListener = onRemoveClickListener;
    this.onAttachClickListener = onAttachClickListener;
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemEvidenceBinding binding = ItemEvidenceBinding.inflate(inflater, parent, false);
    binding.clear.setVisibility(lead ? View.VISIBLE : View.INVISIBLE);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return evidences.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    private final ItemEvidenceBinding binding;

    public Holder(@NonNull ItemEvidenceBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      Evidence evidence = evidences.get(position);
      binding.name.setText(evidence.getName());
      binding.userName.setText(evidence.getUser().getDisplayName());
      binding.created.setText(dateFormat.format(evidence.getCreated()));
      binding.getRoot().setOnClickListener((v) -> onClickListener.onClick(evidence));
      binding.clear.setOnClickListener((v) -> onRemoveClickListener.onRemoveClick(evidence));
      binding.addAttachment.setOnClickListener(
          (v) -> onAttachClickListener.onAttach(evidence,v));
    }
  }

  public interface OnRemoveClickListener {

    void onRemoveClick(Evidence evidence);
  }

  public interface OnClickListener {

    void onClick(Evidence evidence);
  }

  public interface OnAttachClickListener {

    void onAttach(Evidence evidence, View view);
  }
}
