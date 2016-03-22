package de.htw_berlin.movation;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;

import de.htw_berlin.movation.persistence.DatabaseHelper;

@EApplication
public class MyApplication extends Application {
    private volatile DatabaseHelper databaseHelper = null;
    @Pref
    Preferences_ preferences;

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

    public Preferences_ getPreferences() {
        return preferences;
    }
}