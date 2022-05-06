package edu.cnm.deepdive.esms;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.esms.service.ESMSServiceProxy;
import edu.cnm.deepdive.esms.service.GoogleSignInService;
import edu.cnm.deepdive.esms.service.NotesDatabase;

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
