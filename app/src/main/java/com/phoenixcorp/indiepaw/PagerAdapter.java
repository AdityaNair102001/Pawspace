package com.phoenixcorp.indiepaw;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    int countTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int countTabs) {
        super(fm, countTabs);
        this.countTabs = countTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){

            case 0:
                PostFragment postFragment = new PostFragment();
                return postFragment;
            case 1:
                BookmarkFragment bookmarkFragment = new BookmarkFragment();
                return bookmarkFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return countTabs;
    }
}
