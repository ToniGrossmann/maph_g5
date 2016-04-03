package de.htw_berlin.movation.persistence;

/**
 * Created by root on 02.01.2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import de.htw_berlin.movation.R;
import de.htw_berlin.movation.persistence.model.Assignment;
import de.htw_berlin.movation.persistence.model.Discount;
import de.htw_berlin.movation.persistence.model.DiscountType;
import de.htw_berlin.movation.persistence.model.Goal;
import de.htw_berlin.movation.persistence.model.GoalCategory;
import de.htw_berlin.movation.persistence.model.Movatar;
import de.htw_berlin.movation.persistence.model.MovatarClothes;
import de.htw_berlin.movation.persistence.model.Mrg_User_MovatarClothes;
import de.htw_berlin.movation.persistence.model.User;
import de.htw_berlin.movation.persistence.model.Vitals;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 * from https://github.com/j256/ormlite-examples/blob/master/android/HelloAndroid/src/com/example/helloandroid/DatabaseHelper.java
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "movation.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 4;

    List<Class> entities = new ArrayList<Class>() {{
        add(Discount.class);
        add(DiscountType.class);
        add(Goal.class);
        add(GoalCategory.class);
        add(Movatar.class);
        add(MovatarClothes.class);
        add(Mrg_User_MovatarClothes.class);
        add(User.class);
        add(Vitals.class);
        add(Assignment.class);
    }};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            for (Class c : entities)
                TableUtils.createTable(connectionSource, c);
            new FillTablesTask().execute();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            for (Class c : entities)
                TableUtils.dropTable(connectionSource, c, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    private class FillTablesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                fillTables();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(getClass().getSimpleName(), "Tables filled");
        }
    }

    private void fillTables() throws SQLException {
        Dao<Discount, Long> discountDao = getGenericDao(Discount.class);
        Dao<DiscountType, Long> discountTypeDao = getGenericDao(DiscountType.class);
        Dao<Goal, Long> goalDao = getGenericDao(Goal.class);
        Dao<GoalCategory, Long> goalCategoryDao = getGenericDao(GoalCategory.class);
        Dao<Movatar, Long> movatarDao = getGenericDao(Movatar.class);
        Dao<MovatarClothes, Long> movatarClothesDao = getGenericDao(MovatarClothes.class);
        Dao<User, Long> userDao = getGenericDao(User.class);
        Dao<Vitals, Long> vitalsDao = getGenericDao(Vitals.class);

        userDao.createIfNotExists(new User(){{firstName = "user";}});

        final GoalCategory shortTrip = goalCategoryDao.createIfNotExists(new GoalCategory() {{
            name = "Kurzstrecke";
        }});
        final GoalCategory longTrip = goalCategoryDao.createIfNotExists(new GoalCategory() {{
            name = "Langstrecke";
        }});
        GoalCategory marathon = goalCategoryDao.createIfNotExists(new GoalCategory() {{
            name = "Marathon";
        }});
/*
        goalDao.createIfNotExists(new Goal(){{
            description = "";
            requirements = "";
            reward = 0;
            runDistance = 0;
            category = null;
        }});
*/
        goalDao.createIfNotExists(new Goal() {{
            description = "Laufe einen Kilometer";
            requirements = "";
            reward = 1;
            runDistance = 1000;
            category = shortTrip;
        }});

        goalDao.createIfNotExists(new Goal() {{
            description = "Laufe zwei Kilometer";
            requirements = "";
            reward = 3;
            runDistance = 2000;
            category = shortTrip;
        }});

        goalDao.createIfNotExists(new Goal() {{
            description = "Laufe drei Kilometer";
            requirements = "";
            reward = 5;
            runDistance = 3000;
            category = shortTrip;
        }});

        goalDao.createIfNotExists(new Goal() {{
            description = "Laufe zehn Kilometer";
            requirements = "";
            reward = 15;
            runDistance = 10000;
            category = longTrip;
        }});

        goalDao.createIfNotExists(new Goal() {{
            description = "Laufe 20 Kilometer";
            requirements = "";
            reward = 40;
            runDistance = 20000;
            category = longTrip;
        }});

        goalDao.createIfNotExists(new Goal() {{
            description = "Laufe einen Marathon (etwa 42,2 Kilometer)";
            requirements = "";
            reward = 100;
            runDistance = 42195;
            category = longTrip;
        }});

        vitalsDao.createIfNotExists(new Vitals(80, new GregorianCalendar(2016, 3, 31, 17, 44).getTime()));
        vitalsDao.createIfNotExists(new Vitals(90, new GregorianCalendar(2016, 3, 31, 17, 45).getTime()));
        vitalsDao.createIfNotExists(new Vitals(100,new GregorianCalendar(2016,3,31,17,46).getTime()));
        vitalsDao.createIfNotExists(new Vitals(110,new GregorianCalendar(2016,3,31,17,47).getTime()));
        vitalsDao.createIfNotExists(new Vitals(118, new GregorianCalendar(2016, 3, 31, 17, 48).getTime()));
        vitalsDao.createIfNotExists(new Vitals(110,new GregorianCalendar(2016,3,31,17,49).getTime()));
        vitalsDao.createIfNotExists(new Vitals(106, new GregorianCalendar(2016, 3, 31, 17, 50).getTime()));
        vitalsDao.createIfNotExists(new Vitals(109,new GregorianCalendar(2016,3,31,17,51).getTime()));
        vitalsDao.createIfNotExists(new Vitals(100,new GregorianCalendar(2016,3,31,17,52).getTime()));
        vitalsDao.createIfNotExists(new Vitals(98,new GregorianCalendar(2016,3,31,17,53).getTime()));
        vitalsDao.createIfNotExists(new Vitals(96,new GregorianCalendar(2016,3,31,17,54).getTime()));
        vitalsDao.createIfNotExists(new Vitals(99,new GregorianCalendar(2016,3,31,17,55).getTime()));
        vitalsDao.createIfNotExists(new Vitals(90,new GregorianCalendar(2016,3,31,17,56).getTime()));
        vitalsDao.createIfNotExists(new Vitals(86,new GregorianCalendar(2016,3,31,17,57).getTime()));
        vitalsDao.createIfNotExists(new Vitals(79,new GregorianCalendar(2016,3,31,17,58).getTime()));
        vitalsDao.createIfNotExists(new Vitals(76,new GregorianCalendar(2016,3,31,17,59).getTime()));
        vitalsDao.createIfNotExists(new Vitals(66,new GregorianCalendar(2016,3,31,18,0).getTime()));

        movatarClothesDao.create(new MovatarClothes("shortSh_fe_unfit", 100, R.drawable.layer5_female_unfit_sporthose_kurz_2));
        movatarClothesDao.create(new MovatarClothes("longSh_fe_unfit", 100, R.drawable.layer5_female_unfit_sporthose_lang_2));
        movatarClothesDao.create(new MovatarClothes("longShirt_fe_unfit", 100, R.drawable.layer6_female_unfit_langarmshirt_nike_2));
        movatarClothesDao.create(new MovatarClothes("shirt_fe_unfit", 100, R.drawable.layer6_female_unfit_tshirt_nike_2));

        movatarClothesDao.create(new MovatarClothes("shortSh_fe_normal", 100, R.drawable.layer5_female_normal_sporthose_kurz_2));
        movatarClothesDao.create(new MovatarClothes("longSh_fe_normal", 100, R.drawable.layer5_female_normal_sporthose_lang_2));
        movatarClothesDao.create(new MovatarClothes("longShirt_fe_normal", 100, R.drawable.layer6_female_normal_langarmshirt_nike_2));
        movatarClothesDao.create(new MovatarClothes("shirt_fe_normal", 100, R.drawable.layer6_female_normal_tshirt_nike_2));

        movatarClothesDao.create(new MovatarClothes("shortSh_fe_fit", 100, R.drawable.layer5_female_fit_sporthose_kurz_2));
        movatarClothesDao.create(new MovatarClothes("longSh_fe_fit", 100, R.drawable.layer5_female_fit_sporthose_lang_2));
        movatarClothesDao.create(new MovatarClothes("longShirt_fe_fit", 100, R.drawable.layer6_female_fit_langarmshirt_nike_2));
        movatarClothesDao.create(new MovatarClothes("shirt_fe_fit", 100, R.drawable.layer6_female_fit_tshirt_nike_2));

        movatarClothesDao.create(new MovatarClothes("shortSh_ma_unfit", 100, R.drawable.layer5_male_unfit_sporthose_kurz_2));
        movatarClothesDao.create(new MovatarClothes("longSh_ma_unfit", 100, R.drawable.layer5_male_unfit_sporthose_lang_2));
        movatarClothesDao.create(new MovatarClothes("longShirt_ma_unfit", 100, R.drawable.layer6_male_unfit_langarmshirt_nike_2));
        movatarClothesDao.create(new MovatarClothes("shirt_ma_unfit", 100, R.drawable.layer6_male_unfit_tshirt_nike_2));

        movatarClothesDao.create(new MovatarClothes("shortSh_ma_normal", 100, R.drawable.layer5_male_normal_sporthose_kurz_2));
        movatarClothesDao.create(new MovatarClothes("longSh_ma_normalt", 100, R.drawable.layer5_male_normal_sporthose_lang_2));
        movatarClothesDao.create(new MovatarClothes("longShirt_ma_normal", 100, R.drawable.layer6_male_normal_langarmshirt_nike_2));
        movatarClothesDao.create(new MovatarClothes("shirt_ma_normal", 100, R.drawable.layer6_male_normal_tshirt_nike_2));

        movatarClothesDao.create(new MovatarClothes("shortSh_ma_fit", 100, R.drawable.layer5_male_fit_sporthose_kurz_2));
        movatarClothesDao.create(new MovatarClothes("longSh_ma_fit", 100, R.drawable.layer5_male_fit_sporthose_lang_2));
        movatarClothesDao.create(new MovatarClothes("longShirt_ma_fit", 100, R.drawable.layer6_male_fit_langarmshirt_nike_2));
        movatarClothesDao.create(new MovatarClothes("shirt_ma_fit", 100, R.drawable.layer6_male_fit_tshirt_nike_2));

    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
/*
    public Dao<SimpleData, Integer> getDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(SimpleData.class);
        }
        return simpleDao;
    }

    public Dao<User, Integer> getUserDao(){
        if(userDao == null)
            try {
                userDao = getDao(User.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return userDao;
    }
*/
    public <T, ID> Dao<T, ID> getGenericDao(Class clazz) {
        Dao<T, ID> dao = null;
        try {
            dao = getDao(clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    /*
    public RuntimeExceptionDao<SimpleData, Integer> getSimpleDataDao() {
        if (simpleRuntimeDao == null) {
            simpleRuntimeDao = getRuntimeExceptionDao(SimpleData.class);
        }
        return simpleRuntimeDao;
    }
*/
    /**
     * Close the database connections and clear any cached DAOs.
     */
   /*
    @Override
    public void close() {
        super.close();
        simpleDao = null;
        simpleRuntimeDao = null;
    }
    */
}