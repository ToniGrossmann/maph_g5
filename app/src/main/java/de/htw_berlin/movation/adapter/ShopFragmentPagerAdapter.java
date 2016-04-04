package de.htw_berlin.movation.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import de.htw_berlin.movation.DiscountFragment_;
import de.htw_berlin.movation.ShopFragment_;

public class ShopFragmentPagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Movatar", "Rabatte" };

    private Context context;
    List<Fragment> fragments = new ArrayList<>();

    public ShopFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        fragments.add(ShopFragment_.builder().build());
        fragments.add(DiscountFragment_.builder().build());
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
