package de.htw_berlin.movation;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.User;
import de.htw_berlin.movation.persistence.model.Vitals;


@EFragment(R.layout.fragment_statistic)
public class StatisticFragment extends Fragment {

    @FragmentArg
    long mVitalsId = -1;

    private DatabaseHelper dbHelper;

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Vitals, Long> vitalsDao;

    @App
    MyApplication app;

    public StatisticFragment() {}

    @ViewById
    LineChart chart;

    @Pref
    Preferences_ preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = app.getHelper();

        // Datenbank Befüllung
        try {
            vitalsDao.executeRaw("delete from Vitals"); // Dumm aber Zweckmäßig
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
        }
        catch (SQLException e)
        {

        }

    }

    @AfterViews
    public void initData()
    {
        HighlightMarkerView mv = new HighlightMarkerView(this.getActivity(), R.layout.diagramm_markerview);
        chart.setMarkerView(mv);

        YAxis rightAxis = chart.getAxisRight();
        YAxis leftAxis = chart.getAxisLeft();
        XAxis xAxis = chart.getXAxis();

        // Macht die Axen unsichtbar.
        xAxis.setEnabled(false);
        rightAxis.setEnabled(false);

        // Besondere Linie
        LimitLine ll = new LimitLine(80f, "Ruhepuls");
        leftAxis.addLimitLine(ll);

        LineData data = new LineData();

        chart.setData(data);


    }

    @Override
    public void onStart()
    {
        super.onStart();

        TextView pulseMaxValue = (TextView)getView().findViewById(R.id.pulseMaxValue);
        TextView pulseMinValue = (TextView)getView().findViewById(R.id.pulseMinValue);
        TextView totalCreditsEarned = (TextView)getView().findViewById(R.id.earnedCreditsAllTImeValue);
        TextView totalGoalsSuccessfull = (TextView)getView().findViewById(R.id.successfullGoalsValue);

        if(preferences.maxPulse().exists())
            pulseMaxValue.setText(Integer.toString(preferences.maxPulse().get()));
        else
            pulseMaxValue.setText("0");

        if(preferences.minPulse().exists())
            pulseMinValue.setText(Integer.toString(preferences.minPulse().get()));
        else
            pulseMinValue.setText("0");

        if(preferences.creditsEarnedLifeTime().exists())
            totalCreditsEarned.setText(Integer.toString(preferences.creditsEarnedLifeTime().get()));
        else
            totalCreditsEarned.setText("0");

        if(preferences.successfullGoals().exists())
            totalGoalsSuccessfull.setText(Integer.toString(preferences.successfullGoals().get()));
        else
            totalGoalsSuccessfull.setText("0");

        //chart.clear();
        try {
            List<Vitals> vitalList = vitalsDao.queryForAll();

            for(int i = 0; i < vitalList.size(); i++)
            {
                addEntry(vitalList.get(i).pulse,Integer.toString(i));
            }
        }
        catch(SQLException e)
        {}

        chart.invalidate();

    }

    public void addEntry(float pulsValue, String timeStamp) {

        LineData data = chart.getData();

        if (data != null)
        {
            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            // add a new x-value first
            data.addXValue(timeStamp);
            data.addEntry(new Entry(pulsValue, set.getEntryCount()), 0);


            // let the chart know it's data has changed
            chart.notifyDataSetChanged();

            // limit the number of visible entries
            chart.setVisibleXRangeMaximum(120);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            chart.moveViewToX(data.getXValCount() - 121);

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Puls-Daten");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        //set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        //set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(9f);
        set.setDrawValues(false);

        return set;
    }
}