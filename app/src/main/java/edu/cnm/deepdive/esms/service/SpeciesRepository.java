package edu.cnm.deepdive.esms.service;

import android.content.Context;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.model.entity.User;
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

  public Single<List<SpeciesCase>> getAll() {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap(serviceProxy::getAllCases);
  }

  public Single<SpeciesCase> getSpecies(UUID id) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> serviceProxy.getSpeciesCase(id, token));
  }

  public Single<List<User>> getTeam(UUID id) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> serviceProxy.getCaseTeam(id, token));
  }

  public Single<SpeciesCase> saveSpecies(SpeciesCase speciesCase) {
    Function<String, Single<SpeciesCase>> task = (speciesCase.getId() != null)
        ? (token) -> serviceProxy.updateSpecies(speciesCase.getId(), speciesCase, token)
        : (token) -> serviceProxy.addSpecies(speciesCase, token);
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap(task);
  }

  public Single<Boolean> setTeamMember(UUID speciesId, UUID userId, boolean inTeam) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> serviceProxy.setTeamMember(speciesId, userId, inTeam, token));
  }

}
