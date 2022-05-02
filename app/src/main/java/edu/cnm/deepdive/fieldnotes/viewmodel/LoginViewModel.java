package edu.cnm.deepdive.fieldnotes.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.fieldnotes.service.GoogleSignInService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
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
    refresh();
  }

  public LiveData<GoogleSignInAccount> getAccount() {
    return account;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void refresh() {
    throwable.setValue(null);
    Disposable disposable = signInService
        .refresh()
        .subscribe(
            account::postValue,
            (throwable) -> account.postValue(null)
        );
    pending.add(disposable);
  }

  public void startSignIn(ActivityResultLauncher<Intent> launcher) {
    signInService.startSignIn(launcher);
  }

  public void completeSignIn(ActivityResult result) {
    throwable.setValue(null);
    pending.add(
        signInService
            .completeSignIn(result)
            .subscribe(
                account::postValue,
                this::postThrowable
            )
    );
  }

  public void signOut() {
    throwable.setValue(null);
    Disposable disposable = signInService
        .signOut()
        .doFinally(() -> account.postValue(null))
        .subscribe(
            () -> {},
            this::postThrowable
        );
    pending.add(disposable);
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStop(owner);
    pending.clear();
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }
}
