package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class LanguageSelectionActivity extends AppCompatActivity {
    MediaPlayer optionClickMediaPlayer;
    String category;
    NumberPicker difficultyLevelPicker;
    ArrayList<Question> allQuestions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        difficultyLevelPicker=findViewById(R.id.DifficultyLevelPicker);
        TextView heading=findViewById(R.id.heading);

        difficultyLevelPicker.setTranslationY(300);
        heading.setTranslationX(300);

        difficultyLevelPicker.setAlpha(0);
        heading.setAlpha(0);

        difficultyLevelPicker.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        heading.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();


        difficultyLevelPicker.setAlpha(0);
        difficultyLevelPicker.setValue(0);
        difficultyLevelPicker.setMinValue(0);
        difficultyLevelPicker.setMaxValue(1);

        difficultyLevelPicker.setDisplayedValues(new String[]{"English","Urdu"});

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
    public void MoveToQuizActivity(View view){
        playSoundOnButtonClick(view);
        category=getIntent().getStringExtra("category");
        String lang;
        if(difficultyLevelPicker.getValue()==0)
            lang="English";
        else
            lang="urdu";

        Intent intent=new Intent(this, QuizActivity.class);
        intent.putExtra("category",category);
        intent.putExtra("lang",lang);
        startActivity(intent);
    }
    public void playSoundOnButtonClick(View view) {
        optionClickMediaPlayer= MediaPlayer.create(this,R.raw.click);
        optionClickMediaPlayer.start();
        optionClickMediaPlayer.setLooping(false);
        optionClickMediaPlayer.setOnCompletionListener(new  MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                optionClickMediaPlayer.reset();
                optionClickMediaPlayer.release();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}