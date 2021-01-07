package com.example.android.glass.glasstest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MonitorActivity extends BaseActivity {

    private BarChart chart, barChart2;
    private ArrayList<BarEntry> entries=new ArrayList<>();
    private ArrayList<BarEntry> entries2=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        chart=findViewById(R.id.activity_monitor_barchart);
        barChart2=findViewById(R.id.activity_monitor_barchart2);

        loadData();

        int[] colors=new int[]{Color.LTGRAY, Color.DKGRAY};
        int[] colors2=new int[]{getResources().getColor(R.color.light_pink), getResources().getColor(R.color.dark_pink)};

        BarDataSet barDataSet=new BarDataSet(entries, "");
        barDataSet.setColors(colors);

        BarDataSet barDataSet2=new BarDataSet(entries2, "");
        barDataSet2.setColors(colors2);

        BarData barData=new BarData(barDataSet);
        BarData barData2=new BarData(barDataSet2);
        barData.setBarWidth(0.5f);
        barData2.setBarWidth(0.5f);

        barDataSet.setDrawValues(false);
        barDataSet2.setDrawValues(false);

        chart.setData(barData);
        chart.animateY(2000);
        chart.getXAxis().setDrawLabels(false);
        chart.getXAxis().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);

        barChart2.setData(barData2);
        barChart2.animateY(2000);
        barChart2.getXAxis().setEnabled(false);
        barChart2.getXAxis().setDrawLabels(false);
        barChart2.getAxisRight().setEnabled(false);
        barChart2.getAxisLeft().setEnabled(false);
        barChart2.getDescription().setEnabled(false);
        barChart2.getLegend().setEnabled(false);

        barChart2.disableScroll();
        chart.disableScroll();

    }

    private void loadData(){
        entries.add(new BarEntry(0, new float[]{2, 3}));
        entries.add(new BarEntry(1, new float[]{4, 3}));
        entries.add(new BarEntry(2, new float[]{4, 5}));
        entries.add(new BarEntry(3, new float[]{4, 3}));
        entries.add(new BarEntry(4, new float[]{1, 3}));
        entries.add(new BarEntry(5, new float[]{5, 3}));
        entries.add(new BarEntry(6, new float[]{3, 3}));
        entries.add(new BarEntry(7, new float[]{2, 3}));

        entries2.add(new BarEntry(0, new float[]{4, 5}));
        entries2.add(new BarEntry(1, new float[]{5, 3}));
        entries2.add(new BarEntry(2, new float[]{3, 3}));
        entries2.add(new BarEntry(3, new float[]{4, 3}));
        entries2.add(new BarEntry(4, new float[]{1, 3}));
        entries2.add(new BarEntry(5, new float[]{2, 3}));
        entries2.add(new BarEntry(6, new float[]{2, 3}));
        entries2.add(new BarEntry(7, new float[]{4, 3}));
    }
}