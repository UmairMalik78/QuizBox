package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import soup.neumorphism.NeumorphTextView;

public class CategorySelectionActivity extends AppCompatActivity {
    MediaPlayer optionClickMediaPlayer;
    ArrayList<Question>allQuestions;
    NeumorphTextView islamicCatg,scienCatg,sportCatg,mathCatg,hisCatg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);
        islamicCatg=findViewById(R.id.IslamicCategory);
        sportCatg=findViewById(R.id.SportCategory);
        hisCatg=findViewById(R.id.HistoryCategory);
        scienCatg=findViewById(R.id.ScienceCategory);
        mathCatg=findViewById(R.id.MathCategory);

        islamicCatg.setTranslationY(300);
        sportCatg.setTranslationY(300);
        hisCatg.setTranslationY(300);
        scienCatg.setTranslationY(300);
        mathCatg.setTranslationY(300);

        islamicCatg.setAlpha(0);
        sportCatg.setAlpha(0);
        hisCatg.setAlpha(0);
        scienCatg.setAlpha(0);
        mathCatg.setAlpha(0);

        islamicCatg.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        sportCatg.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        hisCatg.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        scienCatg.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        mathCatg.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

    }
    public void MoveToLanguageSelectionActivity(View view){
        playSoundOnButtonClick(view);
        Intent intent=new Intent(this, LanguageSelectionActivity.class);
        intent.putExtra("category",((TextView)view).getText().toString());
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

}
