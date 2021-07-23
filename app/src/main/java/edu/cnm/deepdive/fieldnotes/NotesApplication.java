package edu.cnm.deepdive.fieldnotes;

import android.app.Application;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.fieldnotes.service.NotesDatabase;
import io.reactivex.schedulers.Schedulers;

public class NotesApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    NotesDatabase.setContext(this);
    NotesDatabase.getInstance()
        .getSpeciesDao()
        .delete()
        .subscribeOn(Schedulers.io())
        .subscribe();
  }
}
