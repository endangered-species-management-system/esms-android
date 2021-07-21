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
            entity = Plant.class,
            parentColumns = "plant_id", childColumns = "plant_id",
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

  @ColumnInfo(name = "plant_id", index = true)
  private Long plantId;

  @ColumnInfo(index = true)
  private Category category;

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

  public Long getPlantId() {
    return plantId;
  }

  public void setPlantId(Long plantId) {
    this.plantId = plantId;
  }

  @NonNull
  public Category getCategory() {
    return category;
  }

  public void setCategory(@NonNull Category category) {
    this.category = category;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public enum Category {
    SPECIMEN,
    LOCATION,
    CONDITIONS;

    @TypeConverter
    public static Integer categoryToInteger(Category category) {
      return (category != null) ? category.ordinal() : null;
    }

    @TypeConverter
    public static Category integerToCategory(Integer category) {
      return (category != null) ? Category.values()[category] : null;
    }
  }

}
