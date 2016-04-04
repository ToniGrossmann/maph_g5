package de.htw_berlin.movation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.j256.ormlite.dao.Dao;
import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.Goal;
import de.htw_berlin.movation.persistence.model.GoalCategory;
import de.htw_berlin.movation.persistence.model.MovatarClothes;
import de.htw_berlin.movation.persistence.model.User;

@EFragment(R.layout.fragment_goals)
public class GoalFragment extends Fragment {
    @App
    MyApplication app;
    BandClient client;
    Intent serviceIntent;

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Goal, Long> goalsDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<GoalCategory, Long> goalCategoriesDao;

    GoalListAdapter listAdapter;

    @ViewById(R.id.expandableListView)
    ExpandableListView expListView;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Background
    void connectToBand(){
        try {
            client.connect().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BandException e) {
            e.printStackTrace();
        }
    }

    @AfterViews
    void bindAdapter() {
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // preparing list data
        //prepareListData();
        prepareListData();

        listAdapter = new GoalListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter

        //BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
        //wenn kein Gerät gefunden wurde
        //client = BandClientManager.getInstance().create(getActivity().getBaseContext(), devices[0]);
        //connectToBand();
        //Log.d(getClass().getSimpleName(), client.getConnectionState().name());
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        List<GoalCategory> listGoalGategories = new ArrayList<>();
        List<Goal> listGoals = new ArrayList<>();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        try {
            listGoalGategories = goalCategoriesDao.queryForAll();
            listGoals = goalsDao.queryForAll();
        }
        catch(Exception e) {}
        // Adding child data
        for(int i = 0; i < listGoalGategories.size(); i++)
        {
            listDataHeader.add(listGoalGategories.get(i).name);
            List<String> listGoalNames = new ArrayList<>();
            for(int j = 0; j < listGoals.size();j++) {
                if(listGoals.get(j).category == listGoalGategories.get(i))
                {
                    listGoalNames.add(listGoals.get(j).description);
                }
            }
            listDataChild.put(listGoalGategories.get(i).name, listGoalNames);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
            //BandService_.intent(getActivity().getApplication()).start();
        serviceIntent = BandService_.intent(app).get();
        app.startService(serviceIntent);
    /*
    } else

        {
            // user has not consented yet, request it
            client.getSensorManager().requestHeartRateConsent(getActivity(), new HeartRateConsentListener() {
                @Override
                public void userAccepted(boolean consentGiven) {
                    if (consentGiven) {
                        BandService_.intent(getActivity()).start();

                    }
                }
            });


        }
        */
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getClass().getSimpleName(), "onStop()");
        //app.stopService(BandService_.intent(app).get());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getSimpleName(), "onDestroy()");
        app.stopService(BandService_.intent(app).get());
    }
}