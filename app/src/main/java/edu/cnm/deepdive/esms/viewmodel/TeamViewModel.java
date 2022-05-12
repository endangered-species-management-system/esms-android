package edu.cnm.deepdive.esms.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.service.SpeciesRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class TeamViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final SpeciesRepository repository;
  private final TreeSet<User> teamBackingSet;
  private final MutableLiveData<Collection<User>> team;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public TeamViewModel(
      @NonNull @NotNull Application application) {
    super(application);
    repository = new SpeciesRepository(application);
    teamBackingSet = new TreeSet<>();
    team = new MutableLiveData<>(teamBackingSet);
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }

  public LiveData<Collection<User>> getTeam() {
    return team;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void fetchTeam(UUID id) {
    throwable.setValue(null);
    repository
        .getTeam(id)
        .subscribe(
            (members) -> {
              teamBackingSet.clear();
              teamBackingSet.addAll(members);
              team.postValue(teamBackingSet);
            },
            this::postThrowable,
            pending
        );
  }

  public void setTeamMember(UUID speciesId, User user, boolean inTeam) {
    throwable.setValue(null);
    repository
        .setTeamMember(speciesId, user.getId(), inTeam)
        .subscribe(
            (assigned) -> {
              if (assigned) {
                teamBackingSet.add(user);
              } else {
                teamBackingSet.remove(user);
              }
              team.postValue(teamBackingSet);
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
