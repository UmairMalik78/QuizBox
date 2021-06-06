package com.example.quizbox;

import android.content.Context;
import android.icu.text.Normalizer;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QuestionRepository {
    static private ArrayList<Question> allQuestions;

/*
    static public ArrayList<Question> GetQuestions(String lang, String category,Context applicationContext) {
        String response;
        // Request a string response from the provided URL.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://umairmalik125-001-site1.ctempurl.com/api/QuestionAPI/getQuestions/english/"+category;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ALC",response);
                        allQuestions=ConvertJsonToList(response);
                        // Display the first 500 characters of the response string.
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ALC",error.toString());
                Toast.makeText(context, "Error Occurred ", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return allQuestions;
    }
*/

    static ArrayList<Question> ConvertJsonToList(String jsonArray) {
        ArrayList<Question> questionArrayList = new ArrayList<Question>();
        Gson gson = new Gson();
        Question[] questionList = gson.fromJson(jsonArray, Question[].class);
        Collections.addAll(questionArrayList, questionList);
        return questionArrayList;
    }
}