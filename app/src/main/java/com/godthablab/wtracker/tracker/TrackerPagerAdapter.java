package com.godthablab.wtracker.tracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrackerPagerAdapter extends FragmentStatePagerAdapter {

private List<Fragment> fragments;
private List<String> titles;

public TrackerPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = new ArrayList<>(4);
        this.titles = new ArrayList<>(4);
}

@NonNull
@Override
public Fragment getItem(int position) {
        return fragments.get(position);
}

public void addFragment(Fragment f, String title) {
        this.fragments.add(f);
        this.titles.add(title);
}

@Override
public int getCount() {
        return fragments.size();
}

@Nullable
@Override
public CharSequence getPageTitle(int position) {
        return titles.get(position);
}

}
