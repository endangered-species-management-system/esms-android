package edu.cnm.deepdive.fieldnotes.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.fieldnotes.model.entity.Plant;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface PlantDao {

  @Insert
  Single<Long> insert(Plant plant);

  @Insert
  Single<List<Long>> insert(Collection<Plant> plants);

  @Insert
  Single<List<Long>> insert (Plant... plants);

  @Update
  Single<Integer> update(Plant plant);

  @Update
  Single<Integer> update(Plant... plants);

  @Update
  Single<Integer> update(Collection<Plant> plants);

  @Delete
  Single<Integer> delete(Plant plant);

  @Delete
  Single<Integer> delete(Plant... plant);

  @Delete
  Single<Integer> delete(Collection<Plant> plant);

  @Query("SELECT * FROM Plant ORDER BY created DESC")
  LiveData<List<Plant>> selectAll();

}
