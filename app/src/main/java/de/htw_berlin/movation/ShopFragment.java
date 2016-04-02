package de.htw_berlin.movation;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.ArrayList;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.DiscountType;
import de.htw_berlin.movation.persistence.model.Vitals;

@EFragment(R.layout.fragment_shop_list)
public class ShopFragment extends Fragment {

    @App
    MyApplication app;

    @ViewById
    ListView listView;

    @Bean
    ListViewAdapter adapter;

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<DiscountType, Long> discountDao;

    @AfterViews
    void bindAdapter() {
        listView.setAdapter(adapter);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @ItemClick
    void listItemClicked(PurchuseItem item) {
        //makeText(this, person.firstName + " " + person.lastName, LENGTH_SHORT).show();
    }
}
