package edu.cnm.deepdive.esms.controller;

import android.app.Dialog;
import android.net.Uri;
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
import com.facebook.stetho.inspector.network.MimeMatcher;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.esms.BuildConfig;
import edu.cnm.deepdive.esms.databinding.FragmentAttachmentDialogBinding;
import edu.cnm.deepdive.esms.model.entity.Attachment;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.viewmodel.EvidenceViewModel;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import java.net.URI;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class AttachmentDialogFragment extends DialogFragment {

  private final String[] okFileExtensions = new String[]{
      "jpg",
      "png",
      "gif",
      "jpeg",
      "tiff",
      "heic",
      "bmp"
  };

  private AlertDialog alertDialog;
  private UUID attachmentId;
  public Attachment attachment;
  private FragmentAttachmentDialogBinding binding;
  private EvidenceViewModel evidenceViewModel;
  private UUID evidenceId;
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

            for (String extension : okFileExtensions) {
              if (attachment.getMimeType().toLowerCase().startsWith("image/")) {
                evidenceViewModel
                    .getBitMap()
                    .observe(owner, binding.resourceDetail::setImageBitmap);
                evidenceViewModel
                    .fetchAttachmentBitmap(speciesCaseId, evidenceId, attachmentId);
                dialogBinding(attachment);
              } else {
                // TODO store attachment

              }

            }
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
        (attachment.getMimeType() != null) ? "Attachment type: " + attachment.getMimeType()
            : "N/A");
    binding.attachmentUrl.setText(
        (attachment.getMimeType() != null) ? "Url: " + getUri(attachment) : "N/A");
    binding.imageDatetime.setText(
        (attachment.getCreated() != null) ? "Created date: " + attachment.getCreated() : "N/A");
  }

  @NotNull
  private String getUri(Attachment attachment) {
    return String.format(BuildConfig.URI_FORMAT,
        BuildConfig.BASE_URL, speciesCaseId, evidenceId, attachment.getId());
  }

  private void fetchAttachment() {
    if (speciesCaseId != null && evidenceId != null && attachmentId != null) {
      evidenceViewModel.fetchAttachment(speciesCaseId, evidenceId, attachmentId);
    }
  }

}