package edu.cnm.deepdive.esms.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import edu.cnm.deepdive.esms.model.entity.Attachment;
import edu.cnm.deepdive.esms.model.entity.Evidence;
import edu.cnm.deepdive.esms.model.entity.User;
import edu.cnm.deepdive.esms.service.SpeciesRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

public class EvidenceViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final SpeciesRepository repository;
  private final PriorityQueue<Evidence> evidencesBackingQueue;
  private final MutableLiveData<Collection<Evidence>> evidences;
  private final MutableLiveData<Evidence> evidence;
  private final MutableLiveData<List<Attachment>> attachments;
  private final MutableLiveData<Attachment> attachment;
  private final MutableLiveData<Bitmap> bitMap;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public EvidenceViewModel(
      @NonNull @NotNull Application application) {
    super(application);
    repository = new SpeciesRepository(application);
    evidencesBackingQueue = new PriorityQueue<>();
    evidences = new MutableLiveData<>(evidencesBackingQueue);
    evidence = new MutableLiveData<>();
    attachment = new MutableLiveData<>();
    attachments = new MutableLiveData<>();
    bitMap = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }

  public LiveData<Collection<Evidence>> getEvidences() {
    return evidences;
  }

  public LiveData<Evidence> getEvidence() {
    return evidence;
  }

  public void setEvidence(Evidence evidence) {
    this.evidence.setValue(evidence);
  }

  public LiveData<Attachment> getAttachment() {
    return attachment;
  }

  public LiveData<List<Attachment>> getAttachments() {
    return attachments;
  }

  public LiveData<Bitmap> getBitMap() {
    return bitMap;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void fetchEvidences(UUID id) {
    throwable.setValue(null);
    repository
        .getEvidences(id)
        .subscribe(
            (evidences) -> {
              evidencesBackingQueue.clear();
              evidencesBackingQueue.addAll(evidences);
              this.evidences.postValue(evidencesBackingQueue);
            },
            this::postThrowable,
            pending
        );
  }

  public void fetchEvidence(UUID speciesId, UUID evidenceId) {
    throwable.setValue(null);
    repository
        .getEvidence(speciesId, evidenceId)
        .subscribe(
            evidence::postValue,
            this::postThrowable,
            pending
        );
  }

  public void addEvidence(UUID speciesId, Evidence evidence) {
    throwable.setValue(null);
    repository
        .addEvidence(speciesId, evidence)
        .subscribe(
            (ev) -> {
              this.evidence.postValue(ev);
              evidencesBackingQueue.add(ev);
              evidences.postValue(evidencesBackingQueue);
            },
            this::postThrowable,
            pending
        );
  }

  public void deleteEvidence(UUID speciesId, Evidence evidence) {
    throwable.setValue(null);
    repository
        .deleteEvidence(speciesId, evidence.getId())
        .subscribe(
            () -> {
              evidencesBackingQueue.remove(evidence);
              evidences.postValue(evidencesBackingQueue);
            },
            this::postThrowable,
            pending
        );
  }

  public void store(UUID speciesCaseId, UUID evidenceId, Uri uri, String title,
      String description) {
    throwable.postValue(null);
    pending.add(
        repository
            .addAttachment(speciesCaseId, evidenceId, uri, title, description)
            .subscribe(
                (attachment) -> loadAttachments(speciesCaseId, evidenceId),
                // TODO explore updating list in place without refreshing.
                this::postThrowable
            )
    );
  }

  public void loadAttachments(UUID speciesCaseId, UUID evidenceId) {
    throwable.postValue(null);
    repository
        .getAttachments(speciesCaseId, evidenceId)
        .subscribe(
            attachments::postValue,
            throwable::postValue,
            pending
        );
  }

  public void fetchAttachment(UUID speciesId, UUID evidenceId, UUID attachmentId) {
    throwable.setValue(null);
    repository
        .getAttachment(speciesId, evidenceId, attachmentId)
        .subscribe(
            attachment::postValue,
            this::postThrowable,
            pending
        );
  }

  public void fetchAttachmentBitmap(UUID speciesId, UUID evidenceId, UUID attachmentId) {
    repository
        .getAttachmentContent(speciesId, evidenceId, attachmentId)
        .subscribe(
            (response) -> {
              try (InputStream inputStream = response.byteStream()) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                this.bitMap.postValue(bitmap);
              }
            },
            this::postThrowable,
            pending
        );
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStop(owner);
    pending.clear();
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
