package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MediaPlayer optionClickMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void MoveToCategorySelectionActivity(View view) {
        playSoundOnButtonClick(view);
        Intent intent = new Intent(this, CategorySelectionActivity.class);
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