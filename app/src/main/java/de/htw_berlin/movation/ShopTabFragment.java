package de.htw_berlin.movation;

import android.app.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Telan on 03.04.2016.
 */
@EFragment(R.layout.fragment_tabcontainer)
public class ShopTabFragment extends Fragment {

    private FragmentActivity myContext;

    @ViewById
    FragmentTabHost tabhost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.fragment_tabcontainer);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tabhost.setup(getActivity(),   getActivity().getSupportFragmentManager() , android.R.id.tabcontent);
        tabhost.addTab(tabhost.newTabSpec("movatar Items").setIndicator("Movatar Items"),
                ShopFragment.class, null);
        tabhost.addTab(tabhost.newTabSpec("discounts").setIndicator("Discounts"),
                ShopFragment.class, null);

        return null;
    }


    @AfterViews
    public void init()
    {

    }

}
