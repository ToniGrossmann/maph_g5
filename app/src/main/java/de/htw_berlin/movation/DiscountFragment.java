package de.htw_berlin.movation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.DiscountType;

@EFragment(R.layout.fragment_shop_discount)
public class DiscountFragment extends Fragment {

    @App
    MyApplication app;

    @ViewById
    ListView listView;

    @FragmentArg
    int mPage;

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<DiscountType, Long> discountTypeDao;

    @Bean
    ListViewAdapter adapter;

    @Pref
    Preferences_ preferences;

    @AfterViews
    void bindAdapter() {
        listView.setAdapter(adapter);
    }

    @ItemClick
    void listViewItemClicked(final DiscountType item)
    {
        final DiscountType insideItem = item;
        new AlertDialog.Builder(getContext())
                .setTitle("Kaufbestätigung")
                .setMessage("Bist du sicher, dass du \"" + insideItem.description + "\" für \"" +
                        insideItem.name + "\" von \"" + insideItem.brand + "\" für den Preis von " +
                        insideItem.price + " Credits kaufen möchtest?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if ((preferences.credits().get() - insideItem.price) >= 0) {
                                    preferences.credits().put(preferences.credits().get() - (int) item.price);
                                    try {
                                        //insideItem.owned = true;
                                        discountTypeDao.update(insideItem);

                                    } catch (Exception e) {
                                    }

                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Erfolg!")
                                            .setMessage("Du hast den Rabatt für \"" + insideItem.name + "\" gekauft!").setNeutralButton(android.R.string.ok,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                } else {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Nicht genug Credits!")
                                            .setMessage("Du hast nicht genügend Credits, um den Rabatt für \"" + insideItem.name + "\" zu kaufen!")
                                            .setNeutralButton(android.R.string.ok,
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    }).show();
                                }
                            }
                        }

                )
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // EMTPY
                            }
                        }
                )
                .show();
                }
    }
