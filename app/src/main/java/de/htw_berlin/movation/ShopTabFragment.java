package de.htw_berlin.movation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Telan on 03.04.2016.
 */
@EFragment
public class ShopTabFragment extends Fragment {

    private FragmentActivity myContext;
    // not injected due to bug with android.R resources
    FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate((R.layout.fragment_tabcontainer), null);
        mTabHost = (FragmentTabHost) layout.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("movatar Items").setIndicator("Movatar Items"),
                ShopFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("discounts").setIndicator("Discounts"),
                ShopFragment.class, null);

        return layout;
    }
}
