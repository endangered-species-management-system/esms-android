package edu.cnm.deepdive.esms.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import edu.cnm.deepdive.esms.controller.EvidenceFragment;
import edu.cnm.deepdive.esms.controller.TeamFragment;
import edu.cnm.deepdive.esms.controller.SpeciesCaseFragment;
import org.jetbrains.annotations.NotNull;

public class VPAdapter extends FragmentStateAdapter {

  private final String[] titles = new String[]{"Unknown", "Team", "Evidence"};

  public VPAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
    super(fragmentActivity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    switch (position) {
      case 0:
        return new SpeciesCaseFragment();
      case 1:
        return new TeamFragment();
      case 2:
        return new EvidenceFragment();
    }
    return new SpeciesCaseFragment();
  }

  @Override
  public int getItemCount() {
    return titles.length;
  }
}
