package de.htw_berlin.movation.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.ArrayList;
import java.util.List;

import de.htw_berlin.movation.Constants;
import de.htw_berlin.movation.Preferences_;
import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.DiscountType;
import de.htw_berlin.movation.persistence.model.MovatarClothes;
import de.htw_berlin.movation.view.DiscountTypeView;
import de.htw_berlin.movation.view.DiscountTypeView_;
import de.htw_berlin.movation.view.ItemView;
import de.htw_berlin.movation.view.ItemView_;

/**
 * Created by Telan on 05.04.2016.
 */
@EBean
public class DiscountViewAdapter extends BaseAdapter{

    private List<DiscountType> itemList;
    private ArrayList<DiscountType> filteredItemList;

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<DiscountType, Long> discountTypeDao;

    @RootContext
    Context context;

    @Pref
    Preferences_ preferences;

    @AfterInject
    void initAdapter() {
        try{
            itemList = discountTypeDao.queryForAll();

        }
        catch(Exception e){}
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DiscountTypeView itemView;
        if (convertView == null) {
            itemView = DiscountTypeView_.build(context);
        } else {
            itemView = (DiscountTypeView) convertView;
        }

        itemView.bind(getItem(position));

        return itemView;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public DiscountType getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
