package edu.cnm.deepdive.esms.service;

import android.content.Context;
import androidx.annotation.NonNull;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.model.entity.User;
import io.reactivex.rxjava3.core.Completable;
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
    return preamble()
        .flatMap(serviceProxy::getAllCases);
  }

  public Single<SpeciesCase> getSpecies(UUID id) {
    return preamble()
        .flatMap((token) -> serviceProxy.getSpeciesCase(id, token));
  }

  public Single<List<User>> getTeam(UUID id) {
    return preamble()
        .flatMap((token) -> serviceProxy.getCaseTeam(id, token));
  }

  public Single<SpeciesCase> saveSpecies(SpeciesCase speciesCase) {
    Function<String, Single<SpeciesCase>> task = (speciesCase.getId() != null)
        ? (token) -> serviceProxy.updateSpecies(speciesCase.getId(), speciesCase, token)
        : (token) -> serviceProxy.addSpecies(speciesCase, token);
    return preamble()
        .flatMap(task);
  }

  public Single<Boolean> setTeamMember(UUID speciesId, UUID userId, boolean inTeam) {
    return preamble()
        .flatMap((token) -> serviceProxy.setTeamMember(speciesId, userId, inTeam, token));
  }

  public Single<Evidence> addEvidence(UUID speciesId, Evidence evidence) {
    return preamble()
        .flatMap((token) -> serviceProxy.addEvidence(speciesId, evidence, token));
  }

  public Single<List<Evidence>> getEvidences(UUID speciesId) {
    return preamble()
        .flatMap((token) -> serviceProxy.getEvidences(speciesId, token));
  }

  public Single<Evidence> getEvidence(UUID speciesId, UUID evidenceId) {
    return preamble()
        .flatMap((token) -> serviceProxy.getEvidence(speciesId, evidenceId, token));
  }

  public Completable deleteEvidence(UUID speciesId, UUID evidenceId) {
    return preamble()
        .flatMapCompletable((token) -> serviceProxy.deleteEvidence(speciesId, evidenceId, token));
  }

  @NonNull
  private Single<String> preamble() {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler);
  }
}
