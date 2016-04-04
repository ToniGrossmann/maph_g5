package de.htw_berlin.movation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ExpandableListView;

import com.j256.ormlite.dao.Dao;
import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

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
    @Bean
    GoalListAdapter listAdapter;


    @ViewById(R.id.expandableListView)
    ExpandableListView expListView;


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
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {

                Goal g = listAdapter.getChild(groupPosition, childPosition);
                return true;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // preparing list data
        //prepareListData();
        //prepareListData();

        //listAdapter = new GoalListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter

        //BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
        //wenn kein Gerät gefunden wurde
        //client = BandClientManager.getInstance().create(getActivity().getBaseContext(), devices[0]);
        //connectToBand();
        //Log.d(getClass().getSimpleName(), client.getConnectionState().name());
    }
}