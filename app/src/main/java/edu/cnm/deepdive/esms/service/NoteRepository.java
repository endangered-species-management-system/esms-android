package edu.cnm.deepdive.esms.service;

import android.content.Context;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.esms.model.dao.NoteDao;
import edu.cnm.deepdive.esms.model.dao.SpeciesDao;
import edu.cnm.deepdive.esms.model.entity.Note;
import edu.cnm.deepdive.esms.model.entity.Note.NoteType;
import edu.cnm.deepdive.esms.model.entity.Species;
import io.reactivex.Single;
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

  public Single<Species> save(Species species) {
    return (
        (species.getId() > 0)
            ? speciesDao
            .update(species)
            .map((ignored) -> species)
            : speciesDao
                .insert(species)
                .map((id) -> {
                  species.setId(id);
                  return species;
                })
    )
        .subscribeOn(Schedulers.io());
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

  public LiveData<List<Note>> getRecentNotes() {
    return noteDao.selectRecentNotes();
  }

  public LiveData<List<Note>> selectBySpecies(long speciesId) {
    return noteDao.selectBySpecies(speciesId);
  }

  public LiveData<List<Note>> getNotesByType(long speciesId, NoteType type) {
    return noteDao.selectBySpeciesAndNoteType(speciesId, type);
  }
}
