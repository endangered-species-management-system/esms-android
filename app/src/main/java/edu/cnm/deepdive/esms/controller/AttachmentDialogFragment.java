package edu.cnm.deepdive.esms.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.esms.BuildConfig;
import edu.cnm.deepdive.esms.databinding.FragmentAttachmentDialogBinding;
import edu.cnm.deepdive.esms.model.entity.Attachment;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.viewmodel.EvidenceViewModel;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import java.util.UUID;

public class AttachmentDialogFragment extends DialogFragment {

  private AlertDialog alertDialog;
  private UUID attachmentId;
  public Attachment attachment;
  private FragmentAttachmentDialogBinding binding;
  private EvidenceViewModel evidenceViewModel;
  private Evidence evidence;
  private UUID evidenceId;
  private SpeciesViewModel speciesViewModel;
  private SpeciesCase speciesCase;
  private UUID speciesCaseId;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      AttachmentDialogFragmentArgs args = AttachmentDialogFragmentArgs.fromBundle(getArguments());
      attachmentId = args.getAttachmentId();
      evidenceId = args.getEvidenceId();
      speciesCaseId = args.getSpeciesCaseId();
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = FragmentAttachmentDialogBinding.inflate(LayoutInflater.from(getContext()));
    alertDialog = new Builder(
        getContext())
        .setView(binding.getRoot())
        .setPositiveButton("close", (dlg, which) -> {
        })
        .create();
    return alertDialog;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ViewModelProvider provider = new ViewModelProvider(getActivity());
    LifecycleOwner owner = getViewLifecycleOwner();
    setupAttachmentViewModel(provider, owner);
  }

  private void setupAttachmentViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    evidenceViewModel = provider.get(EvidenceViewModel.class);
    if (attachmentId != null) {
      evidenceViewModel
          .getAttachment()
          .observe(owner, (attachment) -> {
            this.attachment = attachment;
            // TODO Check the mimetype in the attachment to see what is being fetched. If bitmap type, then
            //  otherwise setup a contentUri to download file into public local storage. Another
            //  viewmodel method will be needed
            evidenceViewModel
                .getBitMap()
                .observe(owner, binding.resourceDetail::setImageBitmap);
            evidenceViewModel
                .fetchAttachmentBitmap(speciesCaseId, evidenceId, attachmentId);
            dialogBinding(attachment);
          });
      fetchAttachment();
    }
  }

  private void dialogBinding(Attachment attachment) {
    if (attachment.getPath() != null) {
      Picasso.get().load(String.format(BuildConfig.CONTENT_FORMAT,
              BuildConfig.BASE_URL, speciesCaseId, evidenceId, attachment.getId()))
          .into(binding.resourceDetail);
    }
    binding.imageTitle.setText(
        (attachment.getTitle() != null) ? attachment.getTitle() : "Untitled");
    binding.imageDescription.setText(
        (attachment.getDescription()) != null ? attachment.getDescription() : "N/A");
    binding.resourceId.setText((attachment.getId() != null) ? "Id: " + attachment.getId() : "N/A");
    binding.imageType.setText(
        (attachment.getMimeType() != null) ? "Image type: " + attachment.getMimeType() : "N/A");
    binding.imageUrl.setText(
        (attachment.getPath() != null) ? "Url: " + attachment.getPath() : "N/A");
    binding.imageDatetime.setText(
        (attachment.getCreated() != null) ? "Created date: " + attachment.getCreated() : "N/A");
  }

  private void fetchAttachment() {
    if (speciesCaseId != null && evidenceId != null && attachmentId != null) {
      evidenceViewModel.fetchAttachment(speciesCaseId, evidenceId, attachmentId);
    }
  }

}