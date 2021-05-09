package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HistoryLevelsActivity extends AppCompatActivity {

    int flag=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_levels);
    }
    public void StartQuizLevel1(View view) {
        //Code according to your logic
        flag=1;
    }
    public void StartQuizLevel2(View view) {
        if(flag==1)
        {
            //Code according to your logic
            flag=2;
        }
        else{
            Toast.makeText(getApplicationContext(), "Level is locked", Toast.LENGTH_SHORT).show();
        }

    }
    public void StartQuizLevel3(View view) {
        if(flag==2)
        {
            //Code according to your logic
        }
        else{
            Toast.makeText(getApplicationContext(), "Level is locked", Toast.LENGTH_SHORT).show();
        }

    }
}