package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

public class CategorySelectionActivity extends AppCompatActivity {
    ArrayList<Question>allQuestions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);
    }
    public void MoveToLanguageSelectionActivity(View view){
     //  GetQuestions("english",((TextView)view).getText().toString());
        Intent intent=new Intent(this, LanguageSelectionActivity.class);
        intent.putExtra("category",((TextView)view).getText().toString());
        startActivity(intent);
    }
    private  ArrayList<Question> GetQuestions(String lang, String category) {
        String response;
        // Request a string response from the provided URL.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://umairmalik125-001-site1.ctempurl.com/api/QuestionAPI/getQuestions/"+lang+"/"+category;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ALC",response);
                        ConvertJsonToList(response);
                        // Display the first 500 characters of the response string.
                        Toast.makeText(CategorySelectionActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ALC",error.toString());
                Toast.makeText(CategorySelectionActivity.this, "Error Occurred ", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return allQuestions;
    }

    private ArrayList<Question> ConvertJsonToList(String jsonArray) {
        ArrayList<Question> questionArrayList = new ArrayList<Question>();
        Gson gson = new Gson();
        Question[] questionList = gson.fromJson(jsonArray, Question[].class);
        Collections.addAll(questionArrayList, questionList);
        return questionArrayList;
    }

}
