package de.htw_berlin.movation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    void connectToBand() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Adding child data
        for (GoalCategory gc : listGoalGategories) {
            listDataHeader.add(gc.name);
            List<String> listGoalNames = new ArrayList<>();
            for (Goal g : listGoals) {
                if (gc.equals(g.category)) {
                    listGoalNames.add(g.description);
                }
            }
            listDataChild.put(gc.name, listGoalNames);
        }
    }
}