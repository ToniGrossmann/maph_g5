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

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;

import de.htw_berlin.movation.persistence.DatabaseHelper;
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

    @ViewById
    TextView pulseAxisLabel;

    @ViewById(R.id.pulseMaxValue)
    TextView pulseMaxValue;

    @ViewById(R.id.pulseMinValue)
    TextView pulseMinValue;

    @ViewById(R.id.earnedCreditsAllTImeValue)
    TextView totalCreditsEarned;

    @ViewById(R.id.successfullGoalsValue)
    TextView totalGoalsSuccessfull;

    @Pref
    Preferences_ preferences;

    private int lastIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = app.getHelper();


    }

    @AfterViews
    public void initData()
    {
        HighlightMarkerView mv = new HighlightMarkerView(this.getActivity(), R.layout.diagramm_markerview);
        chart.setMarkerView(mv);
        chart.setDescription("");

        pulseAxisLabel.setRotation(90);
        YAxis rightAxis = chart.getAxisRight();
        YAxis leftAxis = chart.getAxisLeft();
        XAxis xAxis = chart.getXAxis();

        // Macht die Axen unsichtbar.
        xAxis.setEnabled(false);
        rightAxis.setEnabled(false);

        // Besondere Linie
        LimitLine ll = new LimitLine(80f, "Ruhepuls");
        //leftAxis.addLimitLine(ll);

        LineData data = new LineData();

        chart.setData(data);




        getActivity().setTitle(R.string.title_statistics);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        try {
            List<Vitals> vitalList = vitalsDao.queryForAll();

            for(int i = lastIndex; i < vitalList.size(); i++)
            {
                addEntry(vitalList.get(i).pulse,Integer.toString(i));
                lastIndex = i+1;
            }
        }
        catch(SQLException e)
        {}

        pulseMaxValue.setText(Integer.toString(preferences.maxPulse().get()));
        pulseMinValue.setText(Integer.toString(preferences.minPulse().get()));
        totalCreditsEarned.setText(Integer.toString(preferences.creditsEarnedLifeTime().get()));
        totalGoalsSuccessfull.setText(Integer.toString(preferences.successfulGoals().get()));

        chart.invalidate();

    }

    public void addEntry(float pulsValue, String timeStamp) {

        if(pulsValue < preferences.minPulse().get() || preferences.minPulse().get() == 0)
        {
            preferences.minPulse().put((int)pulsValue);
        }
        if(pulsValue > preferences.maxPulse().get())
        {
            preferences.maxPulse().put((int)pulsValue);
        }

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

        LineDataSet set = new LineDataSet(null, "Puls-Werte");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        //set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        //set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(12f);
        set.setDrawValues(false);

        return set;
    }
}
