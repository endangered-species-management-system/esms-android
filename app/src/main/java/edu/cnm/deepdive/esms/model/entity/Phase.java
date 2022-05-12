package edu.cnm.deepdive.esms.model.entity;

import androidx.annotation.NonNull;

public enum Phase {

  SUBMITTED,
  RESEARCH,
  REVIEW,
  CLOSED;


  @NonNull
  @Override
  public String toString() {
    char firstLetter = Character.toUpperCase(name().charAt(0));
    return firstLetter + name().substring(1).toLowerCase();
  }
}
