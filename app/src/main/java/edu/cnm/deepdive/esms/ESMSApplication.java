package edu.cnm.deepdive.esms;

import android.app.Application;
import androidx.annotation.NonNull;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.esms.service.GoogleSignInService;
import java.io.IOException;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class ESMSApplication extends Application {

  private String bearerToken;

  @Override
  public void onCreate() {
    super.onCreate();
//    setupPicasso();
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
