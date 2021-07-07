package edu.cnm.deepdive.fieldnotes.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import edu.cnm.deepdive.fieldnotes.service.NoteRepository;
import io.reactivex.disposables.CompositeDisposable;

public class MainViewModel extends AndroidViewModel {

  NoteRepository repository;
  private final CompositeDisposable pending;
  private final MutableLiveData<Throwable> throwable;

  public MainViewModel(@NonNull Application application) {
    super(application);
    repository = new NoteRepository(application);
    pending = new CompositeDisposable();
    throwable = new MutableLiveData<>();
  }

  public void saveNote(Note note) {
    pending.add(
        repository.saveNote(note)
            .subscribe(
                (n) -> {
                },
                throwable::postValue
            )
    );
  }

}
