package edu.cnm.deepdive.esms.service;

import android.content.Context;

public class UserRepository {

  private final Context context;
  private final ESMSServiceProxy webService;
  private final GoogleSignInService signInService;


  public UserRepository(Context context) {
    this.context = context;
    signInService = GoogleSignInService.getInstance();
    webService = ESMSServiceProxy.getInstance();
  }


}
