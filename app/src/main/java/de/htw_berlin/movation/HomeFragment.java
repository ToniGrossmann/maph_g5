package de.htw_berlin.movation;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
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

    private BandService bandService;
    private Assignment currentAssignment;
    private float currentRunMeters;
    private int currentHeartRate;

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

    @Override
    public void onResume() {
        super.onResume();
        if(preferences.creditsEarnedLifeTime().get()  >= 1000 && preferences.indexFitness().get() == 2)
        {
            preferences.indexFitness().put(1);
            textViewFitness.setText(R.string.fitnessstatus_normal);

        }
        if(preferences.creditsEarnedLifeTime().get()  >= 10000 && preferences.indexFitness().get() == 1)
        {
            preferences.indexFitness().put(0);
            textViewFitness.setText(R.string.fitnessstatus_fit);

        }
    }

    private class ProgressListener implements BandService.ProgressListener {
        @Override
        public void onNewHeartRateRead(final int heartRate) {
            currentHeartRate = heartRate;
            updateAssignmentProgressTextView(false);
        }

        @Override
        public void onRunMeterIncreased(final float runMeters) {
            currentRunMeters = runMeters;
            updateAssignmentProgressTextView(false);
        }

        @Override
        public void onFinishAssignment() {
            updateAssignmentProgressTextView(true);
            // TODO refresh credits
        }
    }

    @UiThread
    void updateAssignmentProgressTextView(boolean finished) {
        if(finished){
            textViewCurrentGoal.setText(R.string.no_current_goal);
            textViewCredits.setText((preferences.credits().getOr(0).toString()));
            currentRunMeters = 0;
            currentHeartRate = 0;
            currentAssignment = null;
        }
        else {
            if(currentAssignment == null)
                return;
        String sb = getResources().getString(R.string.notification_started_assignment, currentAssignment.goal.description) + "\n" +
                getResources().getString(R.string.notification_run_meters, (int) currentRunMeters) + "\n" +
                getResources().getString(R.string.notification_current_pulse, currentHeartRate);
        textViewCurrentGoal.setText(sb);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bandService != null)
            bandService.registerProgressListener(null);
    }

    @SuppressLint("SetTextI18n")
    @AfterViews
    void afterViews() {

        getActivity().setTitle(R.string.title_home);

        if (preferences.startedAssignmentId().exists()) {
            getActivity().bindService(BandService_.intent(getActivity()).get(), new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    bandService = ((BandService.BandServiceBinder) service).getService();
                    bandService.registerProgressListener(new ProgressListener());
                    currentAssignment = bandService.getCurrentAssignment();
                    currentHeartRate = bandService.getCurrentPulse();
                    currentRunMeters = bandService.getRunMeters();
                    updateAssignmentProgressTextView(false);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) { }
            }, 0);

        } else {
            textViewCurrentGoal.setText(R.string.no_current_goal);
        }

        textViewCredits.setText((preferences.credits().getOr(0).toString()));

        if (preferences.indexFitness().get() == Constants.Fitness.FIT.ordinal())
            textViewFitness.setText(R.string.fitnessstatus_fit);
        else if (preferences.indexFitness().get() == Constants.Fitness.AVERAGE.ordinal())
            textViewFitness.setText(R.string.fitnessstatus_normal);
        else if (preferences.indexFitness().get() == Constants.Fitness.FAT.ordinal())
            textViewFitness.setText(R.string.fitnessstatus_unfit);
    }
}
