package edu.cnm.deepdive.esms.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

@Entity(
    foreignKeys = {
        @ForeignKey(
            entity = Species.class,
            parentColumns = "species_id", childColumns = "species_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Note {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "note_id")
  private long id;

  @NonNull
  @ColumnInfo(index = true)
  private Date created = new Date();

  @ColumnInfo(name = "species_id", index = true)
  private Long speciesId;

  @ColumnInfo(index = true)
  private NoteType type;

  @NonNull
  private String note;

  @Expose
  private String href;

  @Expose
  @SerializedName("name")
  private String imageName;

  @Expose
  private String contentType;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public Date getCreated() {
    return created;
  }

  public void setCreated(@NonNull Date created) {
    this.created = created;
  }

  public Long getSpeciesId() {
    return speciesId;
  }

  public void setSpeciesId(Long speciesId) {
    this.speciesId = speciesId;
  }

  public NoteType getType() {
    return type;
  }

  public void setType(NoteType type) {
    this.type = type;
  }

  @NonNull
  public String getNote() {
    return note;
  }

  public void setNote(@NonNull String note) {
    this.note = note;
  }

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public enum NoteType {
    ATTRIBUTE,
    HABITAT,
    GENERAL;

    @TypeConverter
    public static Integer noteTypeToInteger(NoteType type) {
      return (type != null) ? type.ordinal() : null;
    }

    @TypeConverter
    public static NoteType integerToNoteType(Integer type) {
      return (type != null) ? NoteType.values()[type] : null;
    }
  }

  public NoteType typeName() {
    NoteType[] values = NoteType.values();
    return values[getType().ordinal()];
  }


}
