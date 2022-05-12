package edu.cnm.deepdive.esms.model.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class Evidence implements Comparable<Evidence> {

  @Expose
  private UUID id;

  @Expose
  private Date created;

  @Expose
  private Date updated;

  @Expose
  private String name;

  @Expose
  private String location;

  @Expose
  private String number;

  @Expose
  private String note;

  @Expose
  private SpeciesCase speciesCase;

  @Expose
  private User user;

  @Expose
  private Set<Attachment> attachments;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public SpeciesCase getSpeciesCase() {
    return speciesCase;
  }

  public void setSpeciesCase(SpeciesCase speciesCase) {
    this.speciesCase = speciesCase;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Set<Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(Set<Attachment> attachments) {
    this.attachments = attachments;
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
    } else if (obj instanceof Evidence) {
      Evidence other = (Evidence) obj;
      comparison = (id != null && id.equals(other.id));
    } else {
      comparison = false;
    }
    return comparison;
  }

  @NonNull
  @Override
  public String toString() {
    return number;
  }

  @Override
  public int compareTo(Evidence other) {
    return number.compareToIgnoreCase(other.number);
  }
}
