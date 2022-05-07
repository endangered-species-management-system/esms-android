package edu.cnm.deepdive.esms.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import edu.cnm.deepdive.esms.controller.EvidenceFragment;
import edu.cnm.deepdive.esms.controller.MainFragment;
import edu.cnm.deepdive.esms.controller.TeamFragment;
import edu.cnm.deepdive.esms.controller.SpeciesFragment;

public class VPAdapter extends FragmentStateAdapter {

  private final String[] titles = new String[]{"Unknown", "Team", "Evidence"};

  public VPAdapter(@NonNull MainFragment mainFragment) {
    super(mainFragment);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    switch (position) {
      case 0:
        return new SpeciesFragment();
      case 1:
        return new TeamFragment();
      case 2:
        return new EvidenceFragment();
    }
    return new SpeciesFragment();
  }

  @Override
  public int getItemCount() {
    return titles.length;
  }
}
