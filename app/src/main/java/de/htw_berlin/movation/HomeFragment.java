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

        getActivity().setTitle("Home");
        getActivity().getActionBar();

        if (preferences.hasActiveGoal().get()) {
            textViewCurrentGoal.setText("ZIEL.");
        }
        else
        {
            textViewCurrentGoal.setText("Du hast dir aktuell kein Ziel gesetzt.");
        }

        textViewCredits.setText((preferences.credits().get().toString()));

        if (preferences.indexFitness().get() == Constants.Fitness.FIT.ordinal())
            textViewFitness.setText("Fitnesslevel 3: Du bist richtig fit!");
        else if (preferences.indexFitness().get() == Constants.Fitness.AVERAGE.ordinal())
            textViewFitness.setText("Fitnesslevel 2: Streng dich noch etwas an!");
        else if (preferences.indexFitness().get() == Constants.Fitness.FAT.ordinal())
            textViewFitness.setText("Fitnesslevel 1: Aller Anfang ist schwer!");
    }
}
