package edu.cnm.deepdive.fieldnotes.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.fieldnotes.service.GoogleSignInService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import org.jetbrains.annotations.NotNull;

public class LoginViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final GoogleSignInService signInService;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  public LoginViewModel(
      @NonNull @NotNull Application application) {
    super(application);
    signInService = GoogleSignInService.getInstance();
    account = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
//    refresh();
  }


}
