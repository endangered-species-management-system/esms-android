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
import edu.cnm.deepdive.esms.R;
import edu.cnm.deepdive.esms.databinding.FragmentAttachmentUploadBinding;
import edu.cnm.deepdive.esms.model.entity.Evidence;

public class AttachmentUploadFragment extends DialogFragment implements TextWatcher {

  FragmentAttachmentUploadBinding binding;
  private Uri uri;
  private AlertDialog alertDialog;
  private Evidence evidence;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding =
        FragmentAttachmentUploadBinding.inflate(LayoutInflater.from(getContext()), null, false);
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

  private void checkSubmitConditions() {
    alertDialog
        .getButton(DialogInterface.BUTTON_POSITIVE)
        .setEnabled(
            evidence != null
                && !binding.resourceTitle.getText().toString().trim().isEmpty()
                && !binding.resourceDescription.getText().toString().trim().isEmpty()
        );
  }

  private void upload() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_attachment_upload, container, false);
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void afterTextChanged(Editable editable) {

  }
}