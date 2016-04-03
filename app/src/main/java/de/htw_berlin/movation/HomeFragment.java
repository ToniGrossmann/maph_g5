package de.htw_berlin.movation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.Calendar;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.Assignment;
import de.htw_berlin.movation.persistence.model.Goal;
import de.htw_berlin.movation.persistence.model.User;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    private User mUser;
    @FragmentArg
    long mUserId;
    private DatabaseHelper dbHelper;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<User, Long> userDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Assignment, Long> assignmentDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Goal, Long> goalDao;
    @App
    MyApplication app;
    @Pref
    Preferences_ prefs;

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

    @Click
    void assignmentButton(){
        try {
            final Goal g = goalDao.queryBuilder().where().eq("runDistance", 10).query().get(0);
            Assignment assignment = assignmentDao.createIfNotExists(new Assignment(){{goal =g; time = Calendar.getInstance().getTime();}});
            Intent serviceIntent = BandService_.intent(app).get();
            prefs.startedAssignmentId().put((long) assignment.id);
            app.startService(serviceIntent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
