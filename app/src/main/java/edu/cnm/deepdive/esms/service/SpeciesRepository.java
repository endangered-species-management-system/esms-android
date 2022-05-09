package edu.cnm.deepdive.esms.service;

import android.content.Context;
import edu.cnm.deepdive.esms.model.entity.Species;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;

public class SpeciesRepository {

  private final Context context;
  private final ESMSServiceProxy serviceProxy;
  private final GoogleSignInService signInService;

  public SpeciesRepository(Context context) {
    this.context = context;
    serviceProxy = ESMSServiceProxy.getInstance();
    signInService = GoogleSignInService.getInstance();
  }

  public Single<List<Species>> getAll(){
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap(serviceProxy::getAllCases);
  }
}
