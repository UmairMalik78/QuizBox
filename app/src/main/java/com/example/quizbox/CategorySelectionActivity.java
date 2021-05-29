package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CategorySelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);
    }
<<<<<<< HEAD
    public void MoveToQuizActivitySelectionActivity(View view){

    }

    public void MoveToQuizActivity(View view) {
=======
    public void MoveToQuizActivity(View view){
>>>>>>> 3d0e430987ecf9e230b1b70def861c1bf8a6570c
        Intent intent=new Intent(this, QuizActivity.class);
        intent.putExtra("category",((TextView)view).getText().toString());
        startActivity(intent);
    }
}