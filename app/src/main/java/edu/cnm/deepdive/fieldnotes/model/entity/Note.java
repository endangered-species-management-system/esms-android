package edu.cnm.deepdive.fieldnotes.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
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

  public enum NoteType {
    SEASON,
    LOCATION,
    CONDITIONS;

    @TypeConverter
    public static Integer noteTypeToInteger(NoteType type) {
      return (type != null) ? type.ordinal() : null;
    }

    @TypeConverter
    public static NoteType integerToNoteType(Integer type) {
      return (type != null) ? NoteType.values()[type] : null;
    }
  }

}
