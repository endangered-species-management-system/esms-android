package edu.cnm.deepdive.esms.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import androidx.annotation.NonNull;
import edu.cnm.deepdive.esms.model.entity.Attachment;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import edu.cnm.deepdive.esms.model.entity.SpeciesCase;
import edu.cnm.deepdive.esms.model.entity.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SpeciesRepository {

  private final Context context;
  private final ESMSServiceProxy serviceProxy;
  private final GoogleSignInService signInService;
  private final ExecutorService pool;
  private final Scheduler scheduler;
  private final ContentResolver resolver;
  private final MediaType multipartFormType;

  public SpeciesRepository(Context context) {
    this.context = context;
    serviceProxy = ESMSServiceProxy.getInstance();
    signInService = GoogleSignInService.getInstance();
    pool = Executors.newFixedThreadPool(4);
    scheduler = Schedulers.from(pool);
    resolver = context.getContentResolver();
    multipartFormType = MediaType.parse("multipart/form-data");
  }

  public Single<List<SpeciesCase>> getSpeciesCases() {
    return preamble()
        .flatMap(serviceProxy::getAllCases);
  }

  public Single<SpeciesCase> getSpecies(UUID id) {
    return preamble()
        .flatMap((token) -> serviceProxy.getSpeciesCase(id, token));
  }

  public Single<List<User>> getTeam(UUID id) {
    return preamble()
        .flatMap((token) -> serviceProxy.getCaseTeam(id, token));
  }

  public Single<SpeciesCase> saveSpecies(SpeciesCase speciesCase) {
    Function<String, Single<SpeciesCase>> task = (speciesCase.getId() != null)
        ? (token) -> serviceProxy.updateSpecies(speciesCase.getId(), speciesCase, token)
        : (token) -> serviceProxy.addSpecies(speciesCase, token);
    return preamble()
        .flatMap(task);
  }

  public Single<Boolean> setTeamMember(UUID speciesId, UUID userId, boolean inTeam) {
    return preamble()
        .flatMap((token) -> serviceProxy.setTeamMember(speciesId, userId, inTeam, token));
  }

  public Single<Evidence> addEvidence(UUID speciesId, Evidence evidence) {
    return preamble()
        .flatMap((token) -> serviceProxy.addEvidence(speciesId, evidence, token));
  }

  public Single<List<Evidence>> getEvidences(UUID speciesId) {
    return preamble()
        .flatMap((token) -> serviceProxy.getEvidences(speciesId, token));
  }


  public Single<Evidence> getEvidence(UUID speciesId, UUID evidenceId) {
    return preamble()
        .flatMap((token) -> serviceProxy.getEvidence(speciesId, evidenceId, token));
  }

  public Completable deleteEvidence(UUID speciesId, UUID evidenceId) {
    return preamble()
        .flatMapCompletable((token) -> serviceProxy.deleteEvidence(speciesId, evidenceId, token));
  }

  @SuppressWarnings("BlockingMethodInNonBlockingContext")
  public Single<Attachment> addAttachment(UUID speciesCaseId,UUID evidenceId, Uri uri, String title,
      String description) {
    File[] filesCreated = new File[1];
    return preamble()
        .flatMap((token) -> {
          try (
              Cursor cursor = resolver.query(uri, null, null, null, null);
              InputStream input = resolver.openInputStream(uri);
          ) {
            MediaType type = MediaType.parse(resolver.getType(uri));
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            String filename = cursor.getString(nameIndex);
            File outputDir = context.getCacheDir();
            File outputFile = File.createTempFile("xfer", null, outputDir);
            filesCreated[0] = outputFile;
            Files.copy(input, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            RequestBody fileBody = RequestBody.create(outputFile, type);
            MultipartBody.Part filePart =
                MultipartBody.Part.createFormData("file", filename, fileBody);
            RequestBody titlePart = RequestBody.create(title, multipartFormType);
            if (description != null) {
              RequestBody descriptionPart = RequestBody.create(description, multipartFormType);
              return serviceProxy.post(speciesCaseId,evidenceId, token, filePart, titlePart,
                  descriptionPart);
            } else {
              return serviceProxy.post(speciesCaseId,evidenceId, token, filePart, titlePart);
            }
          }
        })
        .doFinally(() -> {
          if (filesCreated[0] != null) {
            try {
              //noinspection ResultOfMethodCallIgnored
              filesCreated[0].delete();
            } catch (Exception e) {
              Log.e(getClass().getName(), e.getMessage(), e);
            }
          }
        });
  }

  public Single<List<Attachment>> getAttachments(UUID speciesCaseId, UUID evidenceId) {
    return preamble()
        .flatMap((token) -> serviceProxy.getAttachments(speciesCaseId, evidenceId, token));

  }

  @NonNull
  private Single<String> preamble() {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler);
  }
}
