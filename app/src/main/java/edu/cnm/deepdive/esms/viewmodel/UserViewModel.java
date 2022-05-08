package edu.cnm.deepdive.esms.viewmodel;

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
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.service.GoogleSignInService;
import edu.cnm.deepdive.esms.service.UserRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.List;

public class UserViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  // TODO Add UI controller-accessible methods for getting & modifying user profile information.

  private final GoogleSignInService signInService;
  private final UserRepository userRepository;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<User> user;
  private final MutableLiveData<List<User>> users;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public UserViewModel(@NonNull Application application) {
    super(application);
    signInService = GoogleSignInService.getInstance();
    userRepository = new UserRepository(application);
    account = new MutableLiveData<>();
    user = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    users = new MutableLiveData<>();
    refresh();
  }

  public LiveData<List<User>> getUsers() {
    return users;
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
            () -> {
            },
            this::postThrowable
        );
    pending.add(disposable);
  }

  public LiveData<User> getUser() {
    throwable.setValue(null);
    Disposable disposable = userRepository
        .getProfile()
        .subscribe(
            user::postValue,
            this::postThrowable
        );
    pending.add(disposable);
    return user;
  }

  public LiveData<List<User>> getAll() {
    throwable.setValue(null);
    Disposable disposable = userRepository
        .getAll()
        .subscribe(
            users::postValue,
            this::postThrowable
        );
    pending.add(disposable);
    return users;
  }

  public void updateUser(User user) {
    throwable.setValue(null);
    Disposable disposable = userRepository
        .updateProfile(user)
        .subscribe(
            this.user::postValue,
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
