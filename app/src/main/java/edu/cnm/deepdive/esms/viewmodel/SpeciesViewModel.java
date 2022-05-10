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
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class SpeciesViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final SpeciesRepository repository;
  private final MutableLiveData<List<Species>> speciesList;
  private final MutableLiveData<Species> species;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public SpeciesViewModel(
      @NonNull @NotNull Application application) {
    super(application);
    repository = new SpeciesRepository(application);
    speciesList = new MutableLiveData<>();
    species = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }

  public LiveData<List<Species>> getSpeciesList() {
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
            speciesList::postValue,
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
    Disposable disposable = repository
        .saveSpecies(species)
        .subscribe(
            this.species::postValue,
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
