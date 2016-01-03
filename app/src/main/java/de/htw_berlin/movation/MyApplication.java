package de.htw_berlin.movation;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.htw_berlin.movation.persistence.DatabaseHelper;


public class MyApplication extends Application {
    private volatile DatabaseHelper databaseHelper = null;

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