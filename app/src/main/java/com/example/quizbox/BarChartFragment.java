package com.example.quizbox;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

public class BarChartFragment extends Fragment {

    BarChart barChart;
    BarData bData;
    BarDataSet barDataSet;
    ArrayList barEntries;
    ArrayList labelsName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_chart, container, false);

        String option = getArguments().getString("correctOption");
        labelsName=new ArrayList<>();
        barChart=view.findViewById(R.id.barChart);
        getEntries(Integer.parseInt(option));


        barDataSet=new BarDataSet(barEntries,"Data Set");

        Description desc=new Description();
        desc.setText("");
        barChart.setDescription(desc);

        bData=new BarData(barDataSet);
        barChart.setData(bData);
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        barChart.setBackgroundColor(Color.rgb(255, 230, 230));

        //Code for hiding axis of graph
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        //code for showing Xaxis labels
        XAxis xAxis=barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsName));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        
        return view;
    }

    private void getEntries(int opt)
    {
        String[] options={"","A","B","C","D"};
        int upperbound = 4;
        int value=2;
        barEntries=new ArrayList<>();

        for(int i=1;i<=4;i++) {
            Random rand = new Random(); //instance of random class
            //generate random values from 0-3
            int int_random = rand.nextInt(upperbound);
            if(opt==i){
                int_random=upperbound * (int_random+1 );
            }
            barEntries.add(new BarEntry(i,int_random));
            labelsName.add(options[i-1]);

        }
        labelsName.add(options[4]);
        Log.d("ALC","Option"+labelsName);
    }
}