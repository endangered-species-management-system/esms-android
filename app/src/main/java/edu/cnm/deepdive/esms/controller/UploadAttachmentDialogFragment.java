package edu.cnm.deepdive.esms.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.esms.R;
import edu.cnm.deepdive.esms.databinding.FragmentUploadAttachmentDialogBinding;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import edu.cnm.deepdive.esms.viewmodel.EvidenceViewModel;
import edu.cnm.deepdive.esms.viewmodel.SpeciesViewModel;
import java.util.UUID;

public class UploadAttachmentDialogFragment extends DialogFragment implements TextWatcher {

  FragmentUploadAttachmentDialogBinding binding;
  private Uri uri;
  private AlertDialog alertDialog;
  private UUID evidenceId;
  private Evidence evidence;
  private EvidenceViewModel evidenceViewModel;
  private SpeciesViewModel speciesViewModel;
  private UUID speciesCaseId;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    uri = UploadAttachmentDialogFragmentArgs.fromBundle(getArguments()).getContentUri();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding =
        FragmentUploadAttachmentDialogBinding.inflate(LayoutInflater.from(getContext()), null,
            false);
    alertDialog = new Builder(getContext())
        .setIcon(R.drawable.ic_upload)
        .setTitle(R.string.upload_resource)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.cancel, (dlg, which) -> {/* No need to do anything. */})
        .setPositiveButton(android.R.string.ok, (dlg, which) -> upload())
        .create();
    alertDialog.setOnShowListener((dlg) -> {
      binding.resourceTitle.addTextChangedListener(this);
      binding.resourceDescription.addTextChangedListener(this);
      checkSubmitConditions();
    });
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
    Picasso.get()
        .load(uri)
        .into(binding.image);
    binding.resourceTitle.addTextChangedListener(this);
      binding.resourceDescription.addTextChangedListener(this);
    //noinspection ConstantConditions
    evidenceViewModel = new ViewModelProvider(getActivity()).get(EvidenceViewModel.class);
    speciesViewModel = new ViewModelProvider(getActivity()).get(SpeciesViewModel.class);
    speciesViewModel.getSpecies().observe(getViewLifecycleOwner(), (speciesCase) -> {
      this.speciesCaseId = speciesCase.getId();
    });
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void afterTextChanged(Editable editable) {
    checkSubmitConditions();
  }

  private void checkSubmitConditions() {
    alertDialog
        .getButton(DialogInterface.BUTTON_POSITIVE)
        .setEnabled(
            evidence != null
                && !binding.resourceTitle.getText().toString().trim().isEmpty()
                && !binding.resourceDescription.getText().toString().trim().isEmpty()
        );
  }

  @SuppressWarnings("ConstantConditions")
  private void upload() {
    String title = binding.resourceTitle.getText().toString().trim();
    String description = binding.resourceDescription.getText().toString().trim();
    evidenceViewModel.store(speciesCaseId, evidenceId, uri, title,
        (description.isEmpty() ? null : description));
  }
}

