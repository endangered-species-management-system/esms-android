package edu.cnm.deepdive.fieldnotes.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity
public class Plant {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "plant_id")
  private long id;

  @NonNull
  @ColumnInfo(index = true)
  private Date created = new Date();

  @NonNull
  @ColumnInfo(index = true, name = "common_name")
  private String commonName;

  @NonNull
  @ColumnInfo(index = true, name = "scientific_name")
  private String scientificName;

  @NonNull
  @ColumnInfo(name = "min_temp")
  private Integer minTemp;

  @NonNull
  @ColumnInfo(name = "max_temp")
  private Integer maxTemp;

  @NonNull
  @ColumnInfo(name = "days_to_maturity")
  private Integer daysToMaturity;

  @NonNull
  private Integer spacing;

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

  @NonNull
  public String getCommonName() {
    return commonName;
  }

  public void setCommonName(@NonNull String commonName) {
    this.commonName = commonName;
  }

  @NonNull
  public String getScientificName() {
    return scientificName;
  }

  public void setScientificName(@NonNull String scientificName) {
    this.scientificName = scientificName;
  }

  @NonNull
  public Integer getMinTemp() {
    return minTemp;
  }

  public void setMinTemp(@NonNull Integer minTemp) {
    this.minTemp = minTemp;
  }

  @NonNull
  public Integer getMaxTemp() {
    return maxTemp;
  }

  public void setMaxTemp(@NonNull Integer maxTemp) {
    this.maxTemp = maxTemp;
  }

  @NonNull
  public Integer getDaysToMaturity() {
    return daysToMaturity;
  }

  public void setDaysToMaturity(@NonNull Integer daysToMaturity) {
    this.daysToMaturity = daysToMaturity;
  }

  @NonNull
  public Integer getSpacing() {
    return spacing;
  }

  public void setSpacing(@NonNull Integer spacing) {
    this.spacing = spacing;
  }

  @NonNull
  @Override
  public String toString() {
    return commonName;
  }
}
