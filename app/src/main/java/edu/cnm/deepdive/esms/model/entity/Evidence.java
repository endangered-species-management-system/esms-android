package edu.cnm.deepdive.esms.model.entity;

import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class Evidence {

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

  public Set<Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(Set<Attachment> attachments) {
    this.attachments = attachments;
  }
}
