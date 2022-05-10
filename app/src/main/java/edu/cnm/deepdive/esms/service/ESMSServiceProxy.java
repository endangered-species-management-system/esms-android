package edu.cnm.deepdive.esms.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.esms.BuildConfig;
import edu.cnm.deepdive.esms.model.entity.Species;
import edu.cnm.deepdive.esms.model.entity.User;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ESMSServiceProxy {

  @GET("users")
  Single<List<User>> getUsers(@Header("Authorization") String bearerToken);

  @GET("users/me")
  Single<User> getProfile(@Header("Authorization") String bearerToken);

  @PUT("users/me")
  Single<User> updateProfile(@Body User user, @Header("Authorization") String bearerToken);

  @PUT("users/{id}/roles")
  Single<User> updateRoles(@Path("id") UUID id, @Body Set<String> roles,
      @Header("Authorization") String bearerToken);

  @PUT("users/{id}/inactive")
  Single<User> updateInactive(@Path("id") UUID id, @Body boolean inactive,
      @Header("Authorization") String bearerToken);

  @GET("cases")
  Single<List<Species>> getAllCases(@Header("Authorization") String bearerToken);

  @GET("cases/{id}")
  Single<Species> getSpeciesCase(@Path("id") UUID id, @Header("Authorization") String bearerToken);

  @POST("cases")
  Single<Species> addSpecies(@Body Species species, @Header("Authorization") String bearerToken);

  @PUT("cases/{id}")
  Single<Species> updateSpecies(@Path("id") UUID id, @Body Species species,
      @Header("Authorization") String bearerToken);

  static ESMSServiceProxy getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final ESMSServiceProxy INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .client(client)
          .baseUrl(BuildConfig.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
          .build();
      INSTANCE = retrofit.create(ESMSServiceProxy.class);
    }
  }

}
