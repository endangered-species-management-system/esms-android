package edu.cnm.deepdive.esms.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.esms.BuildConfig;
import edu.cnm.deepdive.esms.model.entity.Attachment;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.model.entity.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ESMSServiceProxy {

  @GET("users")
  Single<List<User>> getUsers(@Header("Authorization") String bearerToken);

  @GET("users")
  Single<List<User>> getUsers(@Query("role") String role,
      @Header("Authorization") String bearerToken);

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
  Single<List<SpeciesCase>> getAllCases(@Header("Authorization") String bearerToken);

  @GET("cases/{id}")
  Single<SpeciesCase> getSpeciesCase(@Path("id") UUID id,
      @Header("Authorization") String bearerToken);

  @GET("cases/{id}/team")
  Single<List<User>> getCaseTeam(@Path("id") UUID id, @Header("Authorization") String bearerToken);

  @POST("cases")
  Single<SpeciesCase> addSpecies(@Body SpeciesCase speciesCase,
      @Header("Authorization") String bearerToken);

  @PUT("cases/{id}")
  Single<SpeciesCase> updateSpecies(@Path("id") UUID id, @Body SpeciesCase speciesCase,
      @Header("Authorization") String bearerToken);

  @PUT("cases/{caseId}/team/{userId}")
  Single<Boolean> setTeamMember(@Path("caseId") UUID caseId, @Path("userId") UUID userId,
      @Body boolean inTeam, @Header("Authorization") String bearerToken);

  @POST("cases/{caseId}/evidences")
  Single<Evidence> addEvidence(@Path("caseId") UUID caseId, @Body Evidence evidence,
      @Header("Authorization") String bearerToken);

  @GET("cases/{caseId}/evidences")
  Single<List<Evidence>> getEvidences(@Path("caseId") UUID caseId,
      @Header("Authorization") String bearerToken);

  @GET("cases/{caseId}/evidences/{evidenceId}")
  Single<Evidence> getEvidence(@Path("caseId") UUID caseId, @Path("evidenceId") UUID evidenceId,
      @Header("Authorization") String bearerToken);

  @DELETE("cases/{caseId}/evidences/{evidenceId}")
  Completable deleteEvidence(@Path("caseId") UUID caseId, @Path("evidenceId") UUID evidenceId,
      @Header("Authorization") String bearerToken);

  @Multipart
  @POST("cases/{speciesCaseId}/evidences/{evidenceId}/attachments")
  Single<Attachment> post(@Path("speciesCaseId") UUID speciesCaseId,
      @Path("evidenceId") UUID evidenceId, @Header("Authorization") String bearerToken,
      @Part MultipartBody.Part file, @Part("title") RequestBody title);

  @Multipart
  @POST("cases/{speciesCaseId}/evidences/{evidenceId}/attachments")
  Single<Attachment> post(@Path("speciesCaseId") UUID speciesCaseId,
      @Path("evidenceId") UUID evidenceId, @Header("Authorization") String bearerToken,
      @Part MultipartBody.Part file, @Part("title") RequestBody title,
      @Part("description") RequestBody description);

  @GET("cases/{speciesCaseId}/evidences/{evidenceId}/attachments")
  Single<List<Attachment>> getAttachments(@Path("speciesCaseId") UUID speciesCaseId,
      @Path("evidenceId") UUID evidenceId,
      @Header("Authorization") String bearerToken);

  static ESMSServiceProxy getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final ESMSServiceProxy INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
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
