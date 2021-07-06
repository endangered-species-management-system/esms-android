package edu.cnm.deepdive.fieldnotes.service;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.fieldnotes.model.dao.NoteDao;
import edu.cnm.deepdive.fieldnotes.model.dao.PlantDao;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import edu.cnm.deepdive.fieldnotes.model.entity.Plant;
import edu.cnm.deepdive.fieldnotes.service.NotesDatabase.Converters;
import java.util.Date;

@Database(
    entities = {Plant.class, Note.class},
    version = 1,
    exportSchema = true
)
@TypeConverters(value = {Converters.class, Note.Category.class})
public abstract class NotesDatabase extends RoomDatabase {

  private static final String DB_NAME = "notes_db";

  private static Application context;

  public static void setContext(Application context) {
    NotesDatabase.context = context;
  }

  public static NotesDatabase getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public abstract PlantDao getPlantDao();

  public abstract NoteDao getNoteDao();

  private static class InstanceHolder {

    private static final NotesDatabase INSTANCE =
        Room.databaseBuilder(context, NotesDatabase.class, DB_NAME)
        .build();
  }

  public static class Converters {

    @TypeConverter
    public static Long dateToLong(Date value) {
      return (value != null) ? value.getTime() : null;
    }

    @TypeConverter
    public static Date longToDate(Long value) {
      return (value != null) ? new Date(value) : null;
    }

  }
}
