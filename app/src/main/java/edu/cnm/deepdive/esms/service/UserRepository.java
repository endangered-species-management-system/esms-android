package edu.cnm.deepdive.esms.service;

import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.esms.model.entity.User;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;

public class UserRepository {

  private final Context context;
  private final ESMSServiceProxy serviceProxy;
  private final GoogleSignInService signInService;

  public UserRepository(Context context) {
    this.context = context;
    signInService = GoogleSignInService.getInstance();
    serviceProxy = ESMSServiceProxy.getInstance();
  }

  /**
   * Gets a list of all users.
   * @return
   */
  public Single<List<User>> getAll() {
    return refreshToken()
        .flatMap(serviceProxy::getUsers);
  }

  public Single<User> getProfile() {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap(serviceProxy::getProfile);
  }

  public Single<User> updateProfile(User user) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.updateProfile(user, token));
  }

  public Single<User> updateRoles(User user) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.updateRoles(user.getId(), user.getRoles(), token));
  }

  public Single<User> updateActive(User user) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.updateInactive(user.getId(), !user.isActive(), token));
  }

  /**
   * Refreshes bearertoken for continuous service.
   * @return
   */
  private Single<String> refreshToken() {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io());
  }
}
