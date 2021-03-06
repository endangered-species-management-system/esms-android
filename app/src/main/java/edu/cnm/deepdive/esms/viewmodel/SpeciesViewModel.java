package edu.cnm.deepdive.esms.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.service.SpeciesRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class SpeciesViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final SpeciesRepository repository;
  private final PriorityQueue<SpeciesCase> speciesCaseBackingQueue;
  private final MutableLiveData<Collection<SpeciesCase>> speciesList;
  private final MutableLiveData<SpeciesCase> species;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public SpeciesViewModel(
      @NonNull @NotNull Application application) {
    super(application);
    repository = new SpeciesRepository(application);
    speciesCaseBackingQueue = new PriorityQueue<>();
    speciesList = new MutableLiveData<>(speciesCaseBackingQueue);
    species = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }

  public LiveData<Collection<SpeciesCase>> getSpeciesList() {
    return speciesList;
  }

  public LiveData<SpeciesCase> getSpecies() {
    return species;
  }

  public void setSpecies(SpeciesCase speciesCase) {
    this.species.setValue(speciesCase);
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void fetchSpeciesList() {
    throwable.setValue(null);
    repository
        .getSpeciesCases()
        .subscribe(
            (list) -> {
              speciesCaseBackingQueue.clear();
              speciesCaseBackingQueue.addAll(list);
              speciesList.postValue(speciesCaseBackingQueue);
            },
            this::postThrowable,
            pending
        );
  }

  public void fetchSpecies(UUID id) {
    throwable.setValue(null);
    repository
        .getSpecies(id)
        .subscribe(
            species::postValue,
            this::postThrowable,
            pending
        );
  }

  public void saveSpecies(SpeciesCase speciesCase) {
    throwable.setValue(null);
    repository
        .saveSpecies(speciesCase)
        .subscribe(
            (s) -> {
              this.species.postValue(s);
              speciesCaseBackingQueue.remove(s);
              speciesCaseBackingQueue.add(s);
              speciesList.postValue(speciesCaseBackingQueue);
            },
            this::postThrowable,
            pending
        );
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
