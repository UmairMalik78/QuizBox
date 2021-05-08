package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CategorySelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);
    }
    public void MoveToQuizActivity(View view){
        Intent intent=new Intent(this,CategorySelectionActivity.class);
        intent.putExtra("category",((TextView)view).getText().toString());
        startActivity(intent);
    }
}