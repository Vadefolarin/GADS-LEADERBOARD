package com.adedayo.gadsleaderboard.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {

    Context mContext;
    ArrayList<Fragment> mFragments;

    public FragmentAdapter(@NonNull FragmentManager fm, Context context, ArrayList<Fragment> fragments) {
        super(fm);
        this.mContext = context;
        this.mFragments = fragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


}