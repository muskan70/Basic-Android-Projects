package com.manas.miwokapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context c;
    public SimpleFragmentPagerAdapter(FragmentManager fm,Context c){
        super(fm);
        this.c=c;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return new NumberFragment();
        else if(position==1)
            return new FamilyFragment();
        else if(position==2)
            return new ColorFragment();
        else if(position==3)
            return new PhraseFragment();
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return c.getString(R.string.category_numbers);
        else if(position==1)
            return c.getString(R.string.category_family);
        else if(position==2)
            return c.getString(R.string.category_colors);
        return c.getString(R.string.category_phrases);
    }

    public int getCount()
    {
        return 4;
    }
}
