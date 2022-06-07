package edu.cnm.deepdive.esms.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AttachmentRepository {

  private static final String ATTACHMENT_DIRECTORY = "attachments";

  private final Context context;
  private final File attachmentDirectory;
  private final ContentResolver resolver;

  public AttachmentRepository(Context context) {
    this.context = context;
    attachmentDirectory = new File(context.getDataDir(), ATTACHMENT_DIRECTORY);
    attachmentDirectory.mkdirs();
    resolver = context.getContentResolver();
  }

  public Single<String> storePrivateFile(Uri uri) {
    return Single.create(new SingleOnSubscribe<String>() {
          @Override
          public void subscribe(SingleEmitter<String> emitter) throws Exception {
            try (
                Cursor cursor = resolver.query(uri, null, null, null, null);
                InputStream input = resolver.openInputStream(uri)
            ) {
              int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
              cursor.moveToFirst();
              String filename = cursor.getString(nameIndex);
              File outputFile = File.createTempFile("attch", filename, attachmentDirectory);
              Files.copy(input, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
              emitter.onSuccess(outputFile.getName());
            } catch (IOException e) {
              emitter.onError(e);
            }
          }
        })
        .subscribeOn(Schedulers.io());
  }

  public String resolvePath(String path) {
    return new File(attachmentDirectory, path).getAbsolutePath();
  }
}
