package edu.cnm.deepdive.esms.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity
public class Species {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "species_id")
  private long id;

  @NonNull
  @ColumnInfo(index = true)
  private Date created = new Date();

  @NonNull
  private Integer taxonId;

  private String kingdomName;

  private String phylumName;
  private String className;
  private String orderName;
  private String familyName;
  private String genusName;
  private String scientificName;
  private String taxonomicAuthority;
  private String infra_rank;
  private String infra_name;
  private String population;
  private String category;

  @ColumnInfo(index = true)
  private String mainCommonName;

  private String location;

  private String habitat;

  private String mobilityType;

  private String diet;

  private String lifeSpan;

  private String daysToMaturity;

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

  public Integer getTaxonId() {
    return taxonId;
  }

  public void setTaxonId(Integer taxonId) {
    this.taxonId = taxonId;
  }

  public String getKingdomName() {
    return kingdomName;
  }

  public void setKingdomName(String kingdomName) {
    this.kingdomName = kingdomName;
  }

  public String getPhylumName() {
    return phylumName;
  }

  public void setPhylumName(String phylumName) {
    this.phylumName = phylumName;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getOrderName() {
    return orderName;
  }

  public void setOrderName(String orderName) {
    this.orderName = orderName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getGenusName() {
    return genusName;
  }

  public void setGenusName(String genusName) {
    this.genusName = genusName;
  }

  public String getScientificName() {
    return scientificName;
  }

  public void setScientificName(String scientificName) {
    this.scientificName = scientificName;
  }

  public String getTaxonomicAuthority() {
    return taxonomicAuthority;
  }

  public void setTaxonomicAuthority(String taxonomicAuthority) {
    this.taxonomicAuthority = taxonomicAuthority;
  }

  public String getInfra_rank() {
    return infra_rank;
  }

  public void setInfra_rank(String infra_rank) {
    this.infra_rank = infra_rank;
  }

  public String getInfra_name() {
    return infra_name;
  }

  public void setInfra_name(String infra_name) {
    this.infra_name = infra_name;
  }

  public String getPopulation() {
    return population;
  }

  public void setPopulation(String population) {
    this.population = population;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @NonNull
  public String getMainCommonName() {
    return mainCommonName;
  }

  public void setMainCommonName(@NonNull String mainCommonName) {
    this.mainCommonName = mainCommonName;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getHabitat() {
    return habitat;
  }

  public void setHabitat(String habitat) {
    this.habitat = habitat;
  }

  public String getMobilityType() {
    return mobilityType;
  }

  public void setMobilityType(String mobilityType) {
    this.mobilityType = mobilityType;
  }

  public String getDiet() {
    return diet;
  }

  public void setDiet(String diet) {
    this.diet = diet;
  }

  public String getLifeSpan() {
    return lifeSpan;
  }

  public void setLifeSpan(String lifeSpan) {
    this.lifeSpan = lifeSpan;
  }

  public String getDaysToMaturity() {
    return daysToMaturity;
  }

  public void setDaysToMaturity(String daysToMaturity) {
    this.daysToMaturity = daysToMaturity;
  }

  @NonNull
  @Override
  public String toString() {
    return (getMainCommonName().length() > 1) ? mainCommonName + " (" + scientificName+")" : scientificName;
  }
}
