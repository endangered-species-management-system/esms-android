package edu.cnm.deepdive.fieldnotes.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import edu.cnm.deepdive.fieldnotes.model.entity.Note.Category;
import io.reactivex.Single;
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

  @Query("SELECT * FROM Note WHERE species_id = :speciesId")
  LiveData<List<Note>> selectBySpecies(long speciesId);

  @Query("SELECT * FROM Note WHERE species_id = :speciesId AND category = :category")
  LiveData<List<Note>> selectBySpeciesAndCategory(long speciesId, Category category);
}
