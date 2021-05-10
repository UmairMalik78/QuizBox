package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class IslamicLevelsActivity extends AppCompatActivity {

    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_islamic_levels);
        flag=-1;
    }
    public void StartQuizLevel1(View view) {
        //Code according to your logic
        Log.e("ALC","moving");
        Intent intent=new Intent(this,QuizActivity.class);
        intent.putExtra("category","islamic");
        intent.putExtra("level","1");
        startActivity(intent);
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