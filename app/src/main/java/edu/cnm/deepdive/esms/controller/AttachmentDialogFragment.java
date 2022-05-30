package edu.cnm.deepdive.esms.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
  private Attachment attachment;
  private FragmentAttachmentDialogBinding binding;
  private EvidenceViewModel evidenceViewModel;
  private Evidence evidence;
  private SpeciesViewModel speciesViewModel;
  private SpeciesCase speciesCase;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      AttachmentDialogFragmentArgs args = AttachmentDialogFragmentArgs.fromBundle(getArguments());
      attachmentId = args.getAttachmentId();
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = FragmentAttachmentDialogBinding.inflate(LayoutInflater.from(getContext()));
    if(attachment.getPath() != null) {
      Picasso.get().load(String.format(BuildConfig.CONTENT_FORMAT, attachment.getPath()))
          .into(binding.resourceDetail);
    }
    binding.imageDescription.setText((attachment.getDescription()) != null ? attachment.getDescription() : "N/A");
    binding.resourceId.setText((attachment.getId() != null) ? "Id: " +  attachment.getId() : "N/A");
    binding.imageType.setText((attachment.getMimeType() != null) ? "Image type: " + attachment.getMimeType() : "N/A");
    binding.imageUrl.setText((attachment.getPath() != null) ? "Url: " + attachment.getPath() : "N/A");
    binding.imageDatetime.setText((attachment.getCreated() != null) ? "Created date: " + attachment.getCreated() : "N/A");

    return new AlertDialog.Builder(
        getContext())
          .setTitle((attachment.getTitle() != null) ? attachment.getTitle() : "Untitled")
        .setView(binding.getRoot())
        .setPositiveButton("close", (dlg, which) -> {
        })
        .create();
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
    setupSpeciesViewModel(provider, owner);
    setupEvidenceViewModel(provider, owner);
    setupAttachmentViewModel(provider, owner);
  }

  private void setupSpeciesViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    speciesViewModel = provider.get(SpeciesViewModel.class);
    speciesViewModel
        .getSpecies()
        .observe(owner, (speciesCase) -> {
          this.speciesCase = speciesCase;
          fetchAttachment();
        });
  }

  private void setupEvidenceViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    evidenceViewModel = provider.get(EvidenceViewModel.class);
    evidenceViewModel
        .getEvidence()
        .observe(owner, (evidence) -> {
          this.evidence = evidence;
          fetchAttachment();
        });
  }

  private void setupAttachmentViewModel(ViewModelProvider provider, LifecycleOwner owner) {
    evidenceViewModel = provider.get(EvidenceViewModel.class);
    if (attachmentId != null) {
      evidenceViewModel
          .getAttachment()
          .observe(owner, (attachment) -> {
            this.attachment = attachment;
            fetchAttachment();
          });
    }
  }

  private void fetchAttachment() {
    if (speciesCase != null && evidence != null && attachmentId != null) {
      evidenceViewModel.fetchAttachment(speciesCase.getId(), evidence.getId(), attachmentId);
    }
  }

}