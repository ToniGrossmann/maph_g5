package de.htw_berlin.movation;

/**
 * Created by Telan on 02.04.2016.
 */
import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.ArrayList;
import java.util.List;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.DiscountType;
import de.htw_berlin.movation.persistence.model.MovatarClothes;

@EBean
public class ListViewAdapter extends BaseAdapter {

    private List<MovatarClothes> itemList;
    private ArrayList<MovatarClothes> filteredItemList;

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<MovatarClothes, Long> movatarClothesDao;

    @RootContext
    Context context;

    @Pref
    Preferences_ preferences;

    @AfterInject
    void initAdapter() {
        filteredItemList = new ArrayList<>();
        try{
            itemList = movatarClothesDao.queryForAll();
            filterList();
        }
        catch(Exception e){}
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemView itemView;
        if (convertView == null) {
            itemView = ItemView_.build(context);
        } else {
            itemView = (ItemView) convertView;
        }

        itemView.bind(getItem(position));

        return itemView;
    }

    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    @Override
    public MovatarClothes getItem(int position) {
        return filteredItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void filterList()
    {
        for(int i = 0; i < itemList.size();i++) {
            if(itemList.get(i).owned == false) {
                if (itemList.get(i).sex == Constants.Sex.FEMALE && preferences.indexGender().get() == 0) {
                    if (itemList.get(i).fitness == Constants.Fitness.FAT && preferences.indexFitness().get() == 0) {
                        filteredItemList.add(itemList.get(i));
                    } else if (itemList.get(i).fitness == Constants.Fitness.AVERAGE && preferences.indexFitness().get() == 1) {
                        filteredItemList.add(itemList.get(i));
                    } else if (itemList.get(i).fitness == Constants.Fitness.FIT && preferences.indexFitness().get() == 2) {
                        filteredItemList.add(itemList.get(i));
                    }
                } else if (itemList.get(i).sex == Constants.Sex.MALE && preferences.indexGender().get() == 1) {
                    if (itemList.get(i).fitness == Constants.Fitness.FAT && preferences.indexFitness().get() == 0) {
                        filteredItemList.add(itemList.get(i));
                    } else if (itemList.get(i).fitness == Constants.Fitness.AVERAGE && preferences.indexFitness().get() == 1) {
                        filteredItemList.add(itemList.get(i));
                    } else if (itemList.get(i).fitness == Constants.Fitness.FIT && preferences.indexFitness().get() == 2) {
                        filteredItemList.add(itemList.get(i));
                    }
                }
            }
        }
    }
}
