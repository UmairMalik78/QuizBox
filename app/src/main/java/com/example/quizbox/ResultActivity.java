package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView scoreTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Getting information returned by quiz_activity
        Intent intent=getIntent();
        boolean[]answersStatus=intent.getBooleanArrayExtra("answerStatusList");
        int correctAnswersCount=intent.getIntExtra("correctAnsCount",-1);
        //Getting elements of result activity
        scoreTextView=findViewById(R.id.scoreView);
        scoreTextView.setText(Integer.toString(correctAnswersCount*10));
        RatingBar ratingBar=findViewById(R.id.ratingBar);

        //Displaying information on result activity based on count of correct answers;
        ratingBar.setMax(5);
        ratingBar.setRating(correctAnswersCount/2);


    }

    public void GoToMainActivity(View view) {
        Intent intent=new Intent(ResultActivity.this,CategorySelectionActivity.class);
        startActivity(intent);
    }
}