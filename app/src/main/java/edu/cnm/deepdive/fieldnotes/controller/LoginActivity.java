/*
package edu.cnm.deepdive.fieldnotes.controller;

import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.fieldnotes.R;
import edu.cnm.deepdive.fieldnotes.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

  private ActivityLoginBinding binding;
  private LoginViewModel viewModel;
  private ActivityResultLauncher<Intent> launcher;
  private boolean silent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    getLifecycle().addObserver(viewModel);
    launcher = registerForActivityResult(new StartActivityForResult(), viewModel::completeSignIn);
    silent = true;
    viewModel
        .getAccount()
        .observe(this, (account) -> {*/
/* TODO Handle account *//*
});
    viewModel
        .getThrowable()
        .observe(this, (throwable) -> {*/
/* TODO Handle throwable *//*
});
  }
}*/
