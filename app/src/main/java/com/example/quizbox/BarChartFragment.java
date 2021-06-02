package com.example.quizbox;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
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
    String[]  labels = {"A","B","C","D"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_chart, container, false);

        String option = getArguments().getString("correctOption");

        barChart=view.findViewById(R.id.barChart);
        getEntries(Integer.parseInt(option));

        float groupSpace = 0.04f;
        float barSpace = 0.02f;
        float barWidth = 0.46f;

        barDataSet=new BarDataSet(barEntries,"Data Set");

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));


        bData=new BarData(barDataSet);
        barChart.setData(bData);
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        barChart.setBackgroundColor(Color.rgb(255, 230, 230));
        barChart.getXAxis().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);




        return view;
    }

    private void getEntries(int opt)
    {
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
        }
    }
}