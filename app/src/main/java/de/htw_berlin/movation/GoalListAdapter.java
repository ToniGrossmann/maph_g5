package de.htw_berlin.movation;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.Goal;
import de.htw_berlin.movation.persistence.model.GoalCategory;

/**
 * Created by Telan on 04.04.2016.
 */
@EBean
public class GoalListAdapter extends BaseExpandableListAdapter {

    @SystemService
    LayoutInflater inflater;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Goal>> _listDataChild;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Goal, Long> goalDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<GoalCategory, Long> goalCategoryDao;


    List<String> listDataHeader;
    HashMap<String, List<Goal>> listDataChild;

    /*
        public GoalListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<Goal>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }
    */
    @Override
    public Goal getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                                  .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Goal childGoal = getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.goal_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childGoal.description);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                                  .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.goal_list_category, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @AfterInject
    void initAdapter() {
        prepareListData();
    }

    private void prepareListData() {
        List<GoalCategory> listGoalGategories = new ArrayList<>();
        List<Goal> listGoals = new ArrayList<>();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Goal>>();

        try {
            listGoalGategories = goalCategoryDao.queryForAll();
            listGoals = goalDao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Adding child data
        for (GoalCategory gc : listGoalGategories) {
            listDataHeader.add(gc.name);
            List<Goal> listGoalsLocal = new ArrayList<>();
            for (Goal g : listGoals) {
                if (gc.equals(g.category)) {
                    listGoalsLocal.add(g);
                }
            }
            listDataChild.put(gc.name, listGoalsLocal);
        }
        _listDataHeader = listDataHeader;
        _listDataChild = listDataChild;
    }
}
