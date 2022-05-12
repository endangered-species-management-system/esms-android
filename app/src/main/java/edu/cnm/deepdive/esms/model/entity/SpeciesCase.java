package edu.cnm.deepdive.esms.model.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class SpeciesCase implements Comparable<SpeciesCase> {

  @Expose
  private UUID id;

  @Expose
  private Date created;

  @Expose
  private Date updated;

  @Expose
  private String number;

  @Expose
  @SerializedName(value = "speciesName")
  private String name;

  @Expose
  private Phase phase;

  @Expose
  private String summary;

  @Expose
  private String detailedDescription;

  @Expose
  private User leadResearcher;

  @Expose
  private Set<User> assigned;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Phase getPhase() {
    return phase;
  }

  public void setPhase(Phase phase) {
    this.phase = phase;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getDetailedDescription() {
    return detailedDescription;
  }

  public void setDetailedDescription(String detailedDescription) {
    this.detailedDescription = detailedDescription;
  }

  public User getLeadResearcher() {
    return leadResearcher;
  }

  public void setLeadResearcher(User leadResearcher) {
    this.leadResearcher = leadResearcher;
  }

  public Set<User> getAssigned() {
    return assigned;
  }

  public void setAssigned(Set<User> assigned) {
    this.assigned = assigned;
  }

  @NonNull
  @Override
  public String toString() {
    return name;
  }

  @Override
  public int compareTo(SpeciesCase speciesCase) {
    return number.compareToIgnoreCase(speciesCase.number);
  }

  @Override
  public int hashCode() {
    return (id != null) ? id.hashCode() : 0;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    boolean comparison;
    if (this == obj) {
      comparison = true;
    } else if (obj instanceof SpeciesCase) {
      SpeciesCase other = (SpeciesCase) obj;
      comparison = id != null && other.id != null && id.equals(other.id);
    } else {
      comparison = false;
    }

    return comparison;
  }
}
