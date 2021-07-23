package edu.cnm.deepdive.fieldnotes.service;

import android.content.Context;
import androidx.core.graphics.drawable.IconCompat.IconType;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.fieldnotes.model.dao.NoteDao;
import edu.cnm.deepdive.fieldnotes.model.dao.SpeciesDao;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import edu.cnm.deepdive.fieldnotes.model.entity.Species;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class NoteRepository {

  private final Context context;
  private final NoteDao noteDao;
  private final SpeciesDao speciesDao;

  public NoteRepository(Context context) {
    this.context = context;
    NotesDatabase database = NotesDatabase.getInstance();
    noteDao = database.getNoteDao();
    speciesDao = database.getSpeciesDao();
  }

  public Single<Note> saveNote(Note note) {
    return (
        (note.getId() > 0)
            ? noteDao
            .update(note)
            .map((ignored) -> note)
            : noteDao
                .insert(note)
          .map((id) -> {
            note.setId(id);
            return note;
          })
    )
        .subscribeOn(Schedulers.io());
  }

  public LiveData<List<Species>> getSpeciesList() {
    return speciesDao.selectAll();
  }
}
