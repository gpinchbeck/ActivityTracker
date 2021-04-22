package com.example.activitytrackerv2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int tabNum;

    public PageAdapter(@NonNull FragmentManager fm, int tabNumber) {
        super(fm);
        this.tabNum = tabNumber;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new workoutTab();
            case 1:
                return new dietTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabNum;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
