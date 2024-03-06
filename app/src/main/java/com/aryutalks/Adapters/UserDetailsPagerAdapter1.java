package com.aryutalks.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.aryutalks.FollowersFragment;
import com.aryutalks.FollowingFragment;
import com.aryutalks.UserFollowersFragment;
import com.aryutalks.UserFollowingFragment;

public class UserDetailsPagerAdapter1 extends FragmentPagerAdapter {

    public UserDetailsPagerAdapter1(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UserFollowersFragment();
            case 1:
                return new UserFollowingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Followers";
            case 1:
                return "Following";
            default:
                return "";
        }
    }
}
