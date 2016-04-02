package de.htw_berlin.movation;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.User;

@EActivity
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @App
    MyApplication app;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<User, Long> mUserDao;

    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = app.getHelper();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        try {
            mUserDao.create(new User());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*   # UNUSED
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            default:
                break;
            case R.id.nav_homepage:
                HomeFragment hf = HomeFragment_.builder().mUserId(1).build();
                getSupportFragmentManager().beginTransaction()
                                           .replace(R.id.content_main_framelayout, hf).commit();
                break;
            case R.id.nav_sensors:
                SensorFragment sf = SensorFragment_.builder().mUserId(1).build();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main_framelayout, sf).commit();
                break;
            case R.id.nav_goals:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main_framelayout, TestFragment_.builder().build()).commit();
                break;
            case R.id.nav_stats:
                StatisticFragment statf = StatisticFragment_.builder().mVitalsId(1).build();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main_framelayout, statf).commit();
                break;

            case R.id.nav_movatar:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main_framelayout, MovatarFragment_.builder().build()).commit();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
