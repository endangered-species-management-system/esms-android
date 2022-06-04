package edu.cnm.deepdive.esms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.esms.adapter.EvidenceAdapter.Holder;
import edu.cnm.deepdive.esms.controller.MainFragmentDirections;
import edu.cnm.deepdive.esms.databinding.ItemAttachmentBinding;
import edu.cnm.deepdive.esms.databinding.ItemEvidenceBinding;
import edu.cnm.deepdive.esms.model.entity.Attachment;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import java.text.DateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class EvidenceAdapter extends RecyclerView.Adapter<Holder> {

  private final LayoutInflater inflater;
  private final List<Evidence> evidences;
  private final boolean lead;
  Context context;
  private final OnClickListener onClickListener;
  private final OnRemoveClickListener onRemoveClickListener;
  private final OnAttachClickListener onAttachClickListener;
  private final OnAttachmentItemClickListener onAttachmentItemClickListener;
  private NavController navController;
  private final DateFormat dateFormat;

  public EvidenceAdapter(Context context, Collection<Evidence> evidences, boolean lead,
      OnClickListener onClickListener, OnRemoveClickListener onRemoveClickListener,
      OnAttachClickListener onAttachClickListener,
      OnAttachmentItemClickListener onAttachmentItemClickListener) {
    inflater = LayoutInflater.from(context);
    this.evidences = new LinkedList<>(evidences);
    this.lead = lead;
    this.onClickListener = onClickListener;
    this.onRemoveClickListener = onRemoveClickListener;
    this.onAttachClickListener = onAttachClickListener;
    this.onAttachmentItemClickListener = onAttachmentItemClickListener;
    this.context = context;
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
      binding.getRoot();
      binding.more.setOnClickListener((v) -> showAttachments(false));
      binding.less.setOnClickListener((v) -> showAttachments(true));
    }

    private void bind(int position) {
      Evidence evidence = evidences.get(position);
      binding.name.setText(evidence.getName());
      binding.userName.setText(evidence.getUser().getDisplayName());
      binding.created.setText(dateFormat.format(evidence.getCreated()));
      binding.getRoot().setOnClickListener((v) -> onClickListener.onClick(evidence));
      binding.clear.setOnClickListener((v) -> onRemoveClickListener.onRemoveClick(evidence));
      binding.addAttachment.setOnClickListener(
          (v) -> onAttachClickListener.onAttach(evidence));
      binding.attachmentsContainer.removeAllViews();
      if (!evidence.getAttachments().isEmpty()) {
        for (Attachment attachment : evidence.getAttachments()) {
          ItemAttachmentBinding attachmentBinding = ItemAttachmentBinding.inflate(inflater,
              binding.attachmentsContainer, false);
          attachmentBinding.attachmentTitle.setText(attachment.getTitle());
          attachmentBinding.attachmentDescription.setText(attachment.getDescription());
          binding.attachmentsContainer.addView(attachmentBinding.getRoot());
          attachmentBinding.getRoot()
              .setOnClickListener(
                  (v) -> onAttachmentItemClickListener.onAttachmentItemClick(evidence, attachment));
          showAttachments(true);
        }
      } else {
        binding.attachmentsContainer.setVisibility(View.GONE);
        binding.less.setVisibility(View.GONE);
        binding.more.setVisibility(View.GONE);
      }
    }

    private void showAttachments(boolean collapsed) {
      binding.more.setVisibility(collapsed ? View.VISIBLE : View.GONE);
      binding.less.setVisibility(collapsed ? View.GONE : View.VISIBLE);
      binding.attachmentsContainer.setVisibility(collapsed ? View.GONE : View.VISIBLE);
    }

    private NavController getNavController() {
      if (navController == null) {
        navController = Navigation.findNavController(binding.getRoot());
      }
      return navController;
    }

/*    @Override
    public void onAttachmentItemClick(Evidence evidence, View view) {
      on
    }*/
  }

  public interface OnRemoveClickListener {

    void onRemoveClick(Evidence evidence);
  }

  public interface OnClickListener {

    void onClick(Evidence evidence);
  }

  public interface OnAttachClickListener {

    void onAttach(Evidence evidence);
  }

  public interface OnAttachmentItemClickListener {

    void onAttachmentItemClick(Evidence evidence, Attachment attachment);
  }

}

