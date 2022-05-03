package edu.cnm.deepdive.esms.controller;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import edu.cnm.deepdive.esms.R;

public class AdminFragment extends PreferenceFragmentCompat {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState,String rootKey) {
    setPreferencesFromResource(R.xml.admin_pref, rootKey);
  }
}
