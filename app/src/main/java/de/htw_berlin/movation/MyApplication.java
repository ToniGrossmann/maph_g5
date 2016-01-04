package de.htw_berlin.movation;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.devicon_typeface_library.DevIcon;
import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.foundation_icons_typeface_library.FoundationIcons;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.typeface.GenericFont;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.meteocons_typeface_library.Meteoconcs;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.mikepenz.typeicons_typeface_library.Typeicons;
import com.mikepenz.weather_icons_typeface_library.WeatherIcons;

import de.htw_berlin.movation.persistence.DatabaseHelper;


public class MyApplication extends Application {
    private volatile DatabaseHelper databaseHelper = null;

    @Override
    public void onCreate() {
        super.onCreate();
        /*

        Iconics.registerFont(new FontAwesome());
        Iconics.registerFont(new GoogleMaterial());
        Iconics.registerFont(new CommunityMaterial());
        Iconics.registerFont(new DevIcon());
        Iconics.registerFont(new Entypo());
        Iconics.registerFont(new FoundationIcons());
        Iconics.registerFont(new Ionicons());
        Iconics.registerFont(new MaterialDesignIconic());
        Iconics.registerFont(new Meteoconcs());
        Iconics.registerFont(new Octicons());
        Iconics.registerFont(new WeatherIcons());
        Iconics.registerFont(new Typeicons());
        */
    }

    @Override
    public void onTerminate() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
        super.onTerminate();
    }

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}