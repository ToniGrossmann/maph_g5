package de.htw_berlin.movation;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
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
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.ArrayList;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.DiscountType;
import de.htw_berlin.movation.persistence.model.MovatarClothes;
import de.htw_berlin.movation.persistence.model.Vitals;

@EFragment(R.layout.fragment_shop_list)
public class ShopFragment extends Fragment {

    @App
    MyApplication app;

    @ViewById
    ListView listView;

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<MovatarClothes, Long> movatarClothesDao;

    @Bean
    ListViewAdapter adapter;

    @Pref
    Preferences_ preferences;

    @AfterViews
    void bindAdapter() {
        listView.setAdapter(adapter);
    }

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static ShopFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

    }

    @ItemClick
    void listViewItemClicked(final MovatarClothes item)
    {
        final MovatarClothes insideItem = item;
        new AlertDialog.Builder(getContext())
                .setTitle("Kaufbestätigung")
                .setMessage("Bist du sicher, dass du \"" + insideItem.name + "\" für " + insideItem.price + " Credits kaufen möchtest?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if ((preferences.credits().get() - insideItem.price) >= 0) {
                                    preferences.edit().credits().put(preferences.credits().get() - (int) item.price);
                                    try {
                                        insideItem.owned = true;
                                        movatarClothesDao.update(insideItem);

                                    }
                                    catch(Exception e) {}

                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Erfolg!")
                                            .setMessage("Du hast " + insideItem.name + " gekauft!").setNeutralButton(android.R.string.ok,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });

                                } else {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Nicht genug Credits!!")
                                            .setMessage("Du hast nicht genügend Credits für " + insideItem.name + "!").setNeutralButton(android.R.string.ok,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                }
                                // KAUFEN KAUFEN!
                            }
                    }

                )
                .

                        setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Nööööö
                                    }
                    }

                        )
                            .

                    show();
                }
    }
