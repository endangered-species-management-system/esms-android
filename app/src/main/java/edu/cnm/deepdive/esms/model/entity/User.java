package edu.cnm.deepdive.esms.model.entity;

import com.google.gson.annotations.Expose;
import java.util.Date;
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
}
