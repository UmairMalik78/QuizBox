package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView resultStatusTextView;
    TextView correctAnsCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Getting information returned by quiz_activity
        Intent intent=getIntent();
        boolean[]answersStatus=intent.getBooleanArrayExtra("answerStatusList");
        int correctAnswersCount=intent.getIntExtra("correctAnsCount",-1);
        //Getting elements of result activity

        resultStatusTextView=findViewById(R.id.txtResultStatus);
        correctAnsCountTextView=findViewById(R.id.txtCorrectAnsCount);
        RatingBar ratingBar=findViewById(R.id.ratingBar);

        //Displaying information on result activity based on count of correct answers;
        String excellent="Excellent";
        String good="Good";
        String average="Average";
        String fail="Fail";
        ratingBar.setMax(5);
        ratingBar.setRating(correctAnswersCount/2);
        if(correctAnswersCount>=8)
            resultStatusTextView.setText(excellent);
        else if(correctAnswersCount>=5)
            resultStatusTextView.setText(good);
        else if(correctAnswersCount>=3)
            resultStatusTextView.setText(average);
        else
            resultStatusTextView.setText(fail);
        String correctAnsCount="Correct Answers: "+String.valueOf(correctAnswersCount);
        correctAnsCountTextView.setText(correctAnsCount);
    }

    public void GoToMainActivity(View view) {
        Intent intent=new Intent(ResultActivity.this,CategorySelectionActivity.class);
        startActivity(intent);
    }
}