package edu.cnm.deepdive.esms;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.esms.service.GoogleSignInService;

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
    GoogleSignInService.setContext(this);
  }
}
