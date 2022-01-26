package edu.cnm.deepdive.fieldnotes.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import edu.cnm.deepdive.fieldnotes.model.entity.Note.NoteType;
import io.reactivex.Single;
import java.net.NoRouteToHostException;
import java.util.Collection;
import java.util.List;

@Dao
public interface NoteDao {

  @Insert
  Single<Long> insert(Note note);

  @Insert
  Single<List<Long>> insert(Note... notes);

  @Insert
  Single<List<Long>> insert(Collection<Note> notes);

  @Update
  Single<Integer> update(Note note);

  @Update
  Single<Integer> update(Note... notes);

  @Update
  Single<Integer> update(Collection<Note> notes);

  @Delete
  Single<Integer> delete(Note note);

  @Delete
  Single<Integer> delete(Note... note);

  @Delete
  Single<Integer> delete(Collection<Note> note);

  @Query("SELECT * FROM Note ORDER BY created DESC LIMIT 25")
  LiveData<List<Note>> selectRecentNotes();

  @Query("SELECT * FROM Note WHERE species_id = :speciesId")
  LiveData<List<Note>> selectBySpecies(long speciesId);

  @Query("SELECT * FROM Note WHERE species_id = :speciesId AND type = :noteType")
  LiveData<List<Note>> selectBySpeciesAndNoteType(long speciesId, NoteType noteType);
}

