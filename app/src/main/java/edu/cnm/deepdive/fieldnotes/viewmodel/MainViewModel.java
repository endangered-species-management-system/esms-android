package edu.cnm.deepdive.fieldnotes.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import edu.cnm.deepdive.fieldnotes.model.entity.Species;
import edu.cnm.deepdive.fieldnotes.service.NoteRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

  private final NoteRepository repository;
  private final MutableLiveData<Note> note;
  private final MutableLiveData<Species> species;
  private final MutableLiveData<List<Species>> speciesList;
  private final CompositeDisposable pending;
  private final MutableLiveData<Throwable> throwable;

  public MainViewModel(@NonNull Application application) {
    super(application);
    repository = new NoteRepository(application);
    pending = new CompositeDisposable();
    throwable = new MutableLiveData<>();
    note = new MutableLiveData<>();
    species = new MutableLiveData<>();
    speciesList = new MutableLiveData<>();
  }

  public LiveData<Note> getNote() {
    return note;
  }

  public LiveData<Species> getSpecies() {
    return species;
  }

  public LiveData<List<Species>> loadSpecies() {
    return repository.getSpeciesList();
  }

  public void saveNote(Note note) {
    pending.add(
        repository.saveNote(note)
            .subscribe(
                (n) -> {},
                throwable::postValue
            )
    );
  }

}
