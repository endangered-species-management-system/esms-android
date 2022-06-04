package edu.cnm.deepdive.esms.model.entity;

import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

public class Attachment {

  @Expose
  private UUID id;

  @Expose
  private Date created;

  @Expose
  private String title;

  @Expose
  private String description;

  @Expose
  private String name;

  @Expose
  private String path;

  @Expose
  private String mimeType;

  @Expose
  private Evidence evidence;

  @Expose
  private User user;

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public Evidence getEvidence() {
    return evidence;
  }

  public void setEvidence(Evidence evidence) {
    this.evidence = evidence;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
