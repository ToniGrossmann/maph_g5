package de.htw_berlin.movation;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;


@EFragment(R.layout.fragment_statistic)
public class StatisticFragment extends Fragment {

    @FragmentArg
    long mUserId = -1;

    @App
    MyApplication app;

    public StatisticFragment() {}

    @ViewById
    LineChart chart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LineChart chart = (LineChart) getView().findViewById(R.id.chart);


        /*
        YAxis leftAxis = chart.getAxisLeft();
        LimitLine ll = new LimitLine(140f, "Ruhepuls");
        leftAxis.addLimitLine(ll);


        */
    }

    @AfterViews
    public void initData()
    {
        YAxis leftAxis = chart.getAxisLeft();
        LimitLine ll = new LimitLine(80f, "Ruhepuls");
        ArrayList<Entry> pulsValues = new ArrayList<Entry>();
        ArrayList<Entry> smothedpulsValues = new ArrayList<Entry>();

        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
        pulsValues.add(c1e1);
        Entry c1e2 = new Entry(120.000f, 1); // 1 == quarter 2 ...
        pulsValues.add(c1e2);
        Entry c1e3 = new Entry(110.000f, 2); // 1 == quarter 2 ...
        pulsValues.add(c1e3);
        Entry c1e4 = new Entry(90.000f, 3); // 1 == quarter 2 ...
        pulsValues.add(c1e4);
        Entry c1e5 = new Entry(80.000f, 4); // 1 == quarter 2 ...
        pulsValues.add(c1e5);
        Entry c1e6 = new Entry(90.000f, 5); // 1 == quarter 2 ...
        pulsValues.add(c1e6);
        // and so on ...

        //Entry c2e1 = new Entry(120.000f, 0); // 0 == quarter 1
        //valsComp2.add(c2e1);
        //Entry c2e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
        //valsComp2.add(c2e2);

        LineDataSet setComp1 = new LineDataSet(pulsValues, "Puls-Werte");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        //LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");
        //setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        //dataSets.add(setComp2);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("1"); xVals.add("2"); xVals.add("3"); xVals.add("4");xVals.add("5");xVals.add("6");

        LineData data = new LineData(xVals, dataSets);

        chart.setData(data);
        chart.invalidate();
    }

    private void addEntry(float pulsValue, String timeStamp) {

        LineData data = chart.getData();

        if (data != null) {

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

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }
}
