/*
package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

public class DifficultyLevelSelectionActivity extends AppCompatActivity {
    String category;
    NumberPicker difficultyLevelPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_level_selection);
        difficultyLevelPicker=findViewById(R.id.DifficultyLevelPicker);
        difficultyLevelPicker.setValue(0);
        difficultyLevelPicker.setMinValue(0);
        difficultyLevelPicker.setMaxValue(2);

        difficultyLevelPicker.setDisplayedValues(new String[]{"Easy","Medium","Expert"});

        difficultyLevelPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                difficultyLevelPicker.setValue(newVal);
            }
        });

        //Getting Intent
        Intent intent=getIntent();
        String category=intent.getStringExtra("category");
    }
   /* public void MoveToQuizActivity(View view){
        Intent intent=new Intent(this,QuizActivity.class);
        intent.putExtra("category",category);
        intent.putExtra("level",difficultyLevelPicker.getValue());
    }*/
