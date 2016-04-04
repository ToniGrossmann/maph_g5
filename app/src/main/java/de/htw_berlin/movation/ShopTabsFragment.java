package de.htw_berlin.movation;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.User;

@EFragment(R.layout.fragment_shop_tabs)
public class ShopTabsFragment extends Fragment {

    private User mUser;
    @FragmentArg
    long mUserId;
    private DatabaseHelper dbHelper;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<User, Long> userDao;
    @App
    MyApplication app;

    @ViewById
    ViewPager viewPager;
    @ViewById
    TabLayout tabLayout;
    //@ViewById
    //TextView textViewCredits;
    //@ViewById
    //TextView textViewFitness;
    @Pref
    Preferences_ preferences;

    public ShopTabsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = app.getHelper();
        if (getArguments() != null) {
            try {
                mUser = userDao.queryForId(mUserId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    @AfterViews
    void afterViews() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager.setAdapter(new ShopFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity()));

        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);
        getActivity().setTitle(R.string.title_shop);
    }
}