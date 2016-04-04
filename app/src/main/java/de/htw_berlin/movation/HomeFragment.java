package de.htw_berlin.movation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    @ViewById
    TextView textViewCurrentGoal;
    @ViewById
    TextView textViewCredits;
    @ViewById
    TextView textViewFitness;
    @Pref
    Preferences_ preferences;

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


    @AfterViews
    void afterViews() {

        getActivity().setTitle(R.string.title_home);
        getActivity().getActionBar();

        if (preferences.startedAssignmentId().exists()) {
            textViewCurrentGoal.setText("ZIELZIELZIELZIELZIELZIELZIELZIELZIELZIELZIELZIELZIELZIELZIELZIELZIELZIEL.");
        }
        else
        {
            textViewCurrentGoal.setText(R.string.no_current_goal);
        }

        textViewCredits.setText((preferences.credits().get().toString()));

        if (preferences.indexFitness().get() == Constants.Fitness.FIT.ordinal())
            textViewFitness.setText(R.string.fitnessstatus_fit);
        else if (preferences.indexFitness().get() == Constants.Fitness.AVERAGE.ordinal())
            textViewFitness.setText(R.string.fitnessstatus_normal);
        else if (preferences.indexFitness().get() == Constants.Fitness.FAT.ordinal())
            textViewFitness.setText(R.string.fitnessstatus_unfit);
    }
}
