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
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.ArrayList;
import java.util.List;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.DiscountType;
import de.htw_berlin.movation.persistence.model.MovatarClothes;

@EBean
public class ListViewAdapter extends BaseAdapter {

    private List<MovatarClothes> itemList;

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<MovatarClothes, Long> movatarClothesDao;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        try{
            itemList = movatarClothesDao.queryForAll();
        }
        catch(Exception e){}
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PurchaseItemView itemView;
        if (convertView == null) {
            itemView = PurchaseItemView_.build(context);
        } else {
            itemView = (PurchaseItemView) convertView;
        }

        itemView.bind(getItem(position));

        return itemView;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public MovatarClothes getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
