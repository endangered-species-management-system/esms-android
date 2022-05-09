package edu.cnm.deepdive.esms.model.entity;

import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class User {

  @Expose
  private UUID id;

  @Expose
  private String displayName;

  @Expose
  private String location;

  @Expose
  private String email;

  @Expose
  private String phoneNumber;

  @Expose
  private Date created;

  @Expose
  private Set<String> roles;

  @Expose
  private boolean inactive;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  public boolean isActive() {
    return !inactive;
  }

  public void setActive(boolean active) {
    this.inactive = !active;
  }
}
