package edu.cnm.deepdive.esms.service;

import android.content.Context;
import edu.cnm.deepdive.esms.model.entity.Species;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpeciesRepository {

  private final Context context;
  private final ESMSServiceProxy serviceProxy;
  private final GoogleSignInService signInService;
  private final ExecutorService pool;
  private final Scheduler scheduler;

  public SpeciesRepository(Context context) {
    this.context = context;
    serviceProxy = ESMSServiceProxy.getInstance();
    signInService = GoogleSignInService.getInstance();
    pool = Executors.newFixedThreadPool(4);
    scheduler = Schedulers.from(pool);
  }

  public Single<List<Species>> getAll() {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap(serviceProxy::getAllCases);
  }

  public Single<Species> getSpecies(UUID id) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> serviceProxy.getSpeciesCase(id, token));
  }

  public Single<Species> saveSpecies(Species species) {
    Function<String, Single<Species>> task = (species.getId() != null)
        ? (token) -> serviceProxy.updateSpecies(species.getId(), species, token)
        : (token) -> serviceProxy.addSpecies(species, token);
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap(task);
  }

}
