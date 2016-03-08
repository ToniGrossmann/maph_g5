package de.htw_berlin.movation;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.User;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    private User mUser;
    @FragmentArg
    long mUserId;
    private DatabaseHelper dbHelper;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<User, Long> userDao;
    @App
    MyApplication app;

    public HomeFragment() {}

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
}
