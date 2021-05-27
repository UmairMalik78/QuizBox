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
    public void MoveToDifficultyLevelSelectionActivity(View view){
        Intent intent=new Intent(this, DifficultyLevelSelectionActivity.class);
        intent.putExtra("category",((TextView)view).getText().toString());
        startActivity(intent);
    }
}