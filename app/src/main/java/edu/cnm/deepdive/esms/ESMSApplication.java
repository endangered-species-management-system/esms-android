package edu.cnm.deepdive.esms;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.esms.service.GoogleSignInService;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;

public class ESMSApplication extends Application {

  private String bearerToken;

  @Override
  public void onCreate() {
    super.onCreate();
    setupPicasso();
    Stetho.initializeWithDefaults(this);
    GoogleSignInService.setContext(this);
  }

  private void setupPicasso() {
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor((Chain chain) ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Authorization", bearerToken)
                    .build()
            )
        )
        .build();
    Picasso.setSingletonInstance(
        new Picasso.Builder(this)
            .downloader(new OkHttp3Downloader(client))
            .loggingEnabled(BuildConfig.DEBUG)
            .build()
    );
  }
}
