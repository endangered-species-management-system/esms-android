package edu.cnm.deepdive.esms.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.esms.model.entity.Species;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface SpeciesDao {

  @Insert
  Single<Long> insert(Species species);

  @Insert
  Single<List<Long>> insert(Collection<Species> species);

  @Insert
  Single<List<Long>> insert (Species... species);

  @Update
  Single<Integer> update(Species species);

  @Update
  Single<Integer> update(Species... species);

  @Update
  Single<Integer> update(Collection<Species> species);

  @Delete
  Single<Integer> delete(Species species);

  @Delete
  Single<Integer> delete(Species... species);

  @Delete
  Single<Integer> delete(Collection<Species> species);

  @Query("SELECT * FROM Species ORDER BY created ASC")
  LiveData<List<Species>> selectAll();

}
