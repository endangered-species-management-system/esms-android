package edu.cnm.deepdive.fieldnotes.service;

import android.content.Context;
import androidx.core.graphics.drawable.IconCompat.IconType;
import edu.cnm.deepdive.fieldnotes.model.dao.NoteDao;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NoteRepository {

  private final Context context;
  private final NoteDao noteDao;

  public NoteRepository(Context context) {
    this.context = context;
    NotesDatabase database = NotesDatabase.getInstance();
    noteDao = database.getNoteDao();
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
}
