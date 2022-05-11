package edu.cnm.deepdive.esms.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.esms.model.entity.Species;
import edu.cnm.deepdive.esms.service.SpeciesRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class SpeciesViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final SpeciesRepository repository;
  private final PriorityQueue<Species> speciesBackingQueue;
  private final MutableLiveData<Collection<Species>> speciesList;
  private final MutableLiveData<Species> species;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public SpeciesViewModel(
      @NonNull @NotNull Application application) {
    super(application);
    repository = new SpeciesRepository(application);
    speciesBackingQueue = new PriorityQueue<>();
    speciesList = new MutableLiveData<>(speciesBackingQueue);
    species = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }

  public LiveData<Collection<Species>> getSpeciesList() {
    return speciesList;
  }

  public LiveData<Species> getSpecies() {
    return species;
  }

  public void setSpecies(Species species) {
    this.species.setValue(species);
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void fetchSpeciesList() {
    throwable.setValue(null);
    repository
        .getAll()
        .subscribe(
            (list) -> {
              speciesBackingQueue.addAll(list);
              speciesList.postValue(speciesBackingQueue);
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

  public void saveSpecies(Species species) {
    throwable.setValue(null);
    repository
        .saveSpecies(species)
        .subscribe(
            (s) -> {
              this.species.postValue(s);
              speciesBackingQueue.remove(s);
              speciesBackingQueue.add(s);
              speciesList.postValue(speciesBackingQueue);
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