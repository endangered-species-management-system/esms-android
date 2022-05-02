package edu.cnm.deepdive.fieldnotes;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.fieldnotes.service.GoogleSignInService;
import edu.cnm.deepdive.fieldnotes.service.NotesDatabase;
import io.reactivex.schedulers.Schedulers;

public class ESMSApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Picasso.setSingletonInstance(
        new Picasso.Builder(this)
            .loggingEnabled(BuildConfig.DEBUG)
            .build()
    );
    Stetho.initializeWithDefaults(this);
    NotesDatabase.setContext(this);
    GoogleSignInService.setContext(this);
  }
}
