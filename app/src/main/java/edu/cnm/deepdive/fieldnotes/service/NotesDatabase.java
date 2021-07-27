package edu.cnm.deepdive.fieldnotes.service;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import edu.cnm.deepdive.fieldnotes.R;
import edu.cnm.deepdive.fieldnotes.model.dao.NoteDao;
import edu.cnm.deepdive.fieldnotes.model.dao.SpeciesDao;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import edu.cnm.deepdive.fieldnotes.model.entity.Species;
import edu.cnm.deepdive.fieldnotes.service.NotesDatabase.Converters;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Database(
    entities = {Species.class, Note.class},
    version = 1,
    exportSchema = true
)
@TypeConverters(value = {Converters.class, Note.NoteType.class})
public abstract class NotesDatabase extends RoomDatabase {

  private static final String DB_NAME = "notes_db";

  private static Application context;

  public static void setContext(Application context) {
    NotesDatabase.context = context;
  }

  public static NotesDatabase getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public abstract SpeciesDao getSpeciesDao();

  public abstract NoteDao getNoteDao();

  private static class InstanceHolder {

    private static final NotesDatabase INSTANCE =
        Room.databaseBuilder(context, NotesDatabase.class, DB_NAME)
            .addCallback(new CallBack())
            .build();
  }

  private static class CallBack extends RoomDatabase.Callback {

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      super.onCreate(db);
      insertSpecies();
    }

    private void insertSpecies() {
      try (
          InputStream inputStream = context.getResources().openRawResource(R.raw.species);
          Reader reader = new InputStreamReader(inputStream);
          CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
              .withIgnoreEmptyLines()
              .withIgnoreSurroundingSpaces());
      ) {
        List<Species> speciesList = new LinkedList<>();
        for (CSVRecord record : parser) {
            Species species = new Species();
          try {
            species.setTaxonId(Integer.parseInt(record.get(0)));
            species.setKingdomName(record.get(1));
            species.setPhylumName(record.get(2));
            species.setClassName((record.get(3)));
            species.setOrderName(record.get(4));
            species.setFamilyName(record.get(5));
            species.setGenusName(record.get(6));
            species.setScientificName(record.get(7));
            species.setTaxonomicAuthority(record.get(8));
            species.setInfra_rank(record.get(9));
            species.setInfra_name(record.get(10));
            species.setPopulation(record.get(11));
            species.setCategory(record.get(12));
            species.setMainCommonName(record.get(13));
            species.setLocation(record.get(14));
            species.setHabitat(record.get(15));
            species.setMobilityType(record.get(16));
            species.setDiet(record.get(17));
            species.setLifeSpan(record.get(18));
            species.setDaysToMaturity((record.get(19)));
          } catch (NumberFormatException | IndexOutOfBoundsException e) {
            Log.e(getClass().getSimpleName(), record.toString(), e);

          }
            speciesList.add(species);
        }
        NotesDatabase.getInstance().getSpeciesDao().insert(speciesList)
            .subscribeOn(Schedulers.io())
            .subscribe(
                (ids) -> {
                },
                (throwable) -> Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable)
            );
      } catch (IOException e) {
        Log.e(getClass().getSimpleName(), e.getMessage(), e);
        throw new RuntimeException(e);
      }
    }
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
