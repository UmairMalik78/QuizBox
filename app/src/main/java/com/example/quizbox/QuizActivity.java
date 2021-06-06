package com.example.quizbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity implements OnResponse {
    TextView questionDescription, option1, option2, option3, option4, questionNum, score,timerTextView;
    int currentQuestionNum=1,quizScore=0;
    Button nextBtn,lifeline50_50Btn,lifelineAudiencePollBtn,lifelineSwapBtn;
    Question currentQuestion;
    boolean isAnyOptionSelected=false;
    int MAX_QUESTIONS=10,correctAnsCount=0;
    ArrayList<Question> allQuestions;
    Boolean[] answersStatusList=new Boolean[MAX_QUESTIONS];
    ArrayList<Integer>shuffledIndices;
    ProgressBar progressBar;
    CountDownTimer countDownTimer;
    int timer=0;
    int MAX_TIME_IN_MILLIS=20000;
    int COUNT_DOWN_INTERVAL_IN_MILLIS=1000;
    String correctOptionView;
    int clickDisableFlag=1;
    final int TOTAL_LEVELS=3;
    final int QUESTION_IN_EACH_LEVEL=5;

    @Override
    public void onResponse() {
        //Getting shuffled indices
        shuffledIndices=MyMath.GetShuffleIndices(TOTAL_LEVELS,QUESTION_IN_EACH_LEVEL);
        //Setting First Question
        currentQuestion=allQuestions.get(shuffledIndices.get(currentQuestionNum-1));
        //Setting Elements
        SetQuestionNumber();
        SetQuestionAndOptions();
        SetScore();
        StartCountDown();
        ToggleClickFunctionalityOfOptionsViews();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        //getting all the elements
        questionDescription = findViewById(R.id.questionDescription);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        score = findViewById(R.id.scoreView);
        questionNum = findViewById(R.id.questionNumView);
        progressBar=findViewById(R.id.progress_bar);
        progressBar.setProgress(timer);
        timerTextView=findViewById(R.id.counter);
        // TextView textView=findViewById(R.id.myView);
        // getting category and lang from previous activity
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        String lang = intent.getStringExtra("lang");
        // getting all the questions from API.
        GetQuestions(lang,category);
    }

    public void SetScore()
    {
        score.setText("Score: "+String.valueOf(quizScore));
    }

    public void SetQuestionNumber(){
        String text="Q-"+String.valueOf(currentQuestionNum);
        questionNum.setText(text);
    }

    public void SetQuestionAndOptions(){
        questionDescription.setText(currentQuestion.getQuestionDescription());
        option1.setText(currentQuestion.getOption1());
        option2.setText(currentQuestion.getOption2());
        option3.setText(currentQuestion.getOption3());
        option4.setText(currentQuestion.getOption4());

        option1.setBackground(getResources().getDrawable(R.drawable.custom_textview));
        option2.setBackground(getResources().getDrawable(R.drawable.custom_textview));
        option3.setBackground(getResources().getDrawable(R.drawable.custom_textview));
        option4.setBackground(getResources().getDrawable(R.drawable.custom_textview));

    }

    public void updateAllTheElements(){
        SetQuestionAndOptions();
        SetQuestionNumber();
    }


    public void SetNextQuestionOnScreen(){
        StartCountDown();

        currentQuestionNum=currentQuestionNum+1;
        if(currentQuestionNum==MAX_QUESTIONS){//checks if last question has come?
          //  String text="Finish Quiz";
           // nextBtn.setText(text);
        }else if(currentQuestionNum>MAX_QUESTIONS){
            countDownTimer.cancel();
            MoveToResultActivity();
            return;
        }
        currentQuestion=allQuestions.get(shuffledIndices.get(currentQuestionNum-1));
        updateAllTheElements();
        isAnyOptionSelected=false;

    }
    public void StartCountDown() {
        timer=0;
        countDownTimer = new CountDownTimer(MAX_TIME_IN_MILLIS, COUNT_DOWN_INTERVAL_IN_MILLIS) {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTick(long l) {

                Log.d("ALC", "Current TIMER" + (int) timer * 100 / (MAX_TIME_IN_MILLIS / COUNT_DOWN_INTERVAL_IN_MILLIS));
                timer++;
                timerTextView.setText(String.valueOf(timer));
                progressBar.setProgress((int) timer * 100 / (MAX_TIME_IN_MILLIS / COUNT_DOWN_INTERVAL_IN_MILLIS));
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(100);
                timerTextView.setText("Time Finished");
                SetNextQuestionOnScreen();
            }
        }.start();
    }
    /*public void clearOptions(){
        option1.setBackgroundResource(R.color.white);
        option2.setBackgroundResource(R.color.white);
        option3.setBackgroundResource(R.color.white);
        option4.setBackgroundResource(R.color.white);
    }*/

    private void MoveToResultActivity() {
        Intent intent=new Intent(this,ResultActivity.class);
        intent.putExtra("answerStatusList",answersStatusList);
        intent.putExtra("correctAnsCount",correctAnsCount);
        startActivity(intent);
    }
    public void CheckAnswer(View view){
        ToggleClickFunctionalityOfOptionsViews();
        countDownTimer.cancel();
        String usersAns=((TextView)view).getText().toString();

        if(usersAns.equals(currentQuestion.getAnswer())){
           ((TextView)view).getBackground().setColorFilter(Color.parseColor("#5ECF45"), PorterDuff.Mode.SRC_ATOP);
            correctAnsCount++;
            quizScore+=10;
            answersStatusList[currentQuestionNum-1]=true;
            isAnyOptionSelected=true;
            SetScore();
        }else{
            ((TextView)view).getBackground().setColorFilter(Color.parseColor("#F31C1C"), PorterDuff.Mode.SRC_ATOP);
            isAnyOptionSelected=true;
            answersStatusList[currentQuestionNum-1]=false;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                SetNextQuestionOnScreen();
                ToggleClickFunctionalityOfOptionsViews();
            }
        }, 1000);

    }
    //Disabling clicks when an option is selected
    private void ToggleClickFunctionalityOfOptionsViews(){
        clickDisableFlag=clickDisableFlag*-1;
        if(clickDisableFlag==1){//1 -> means to disable clicks
            option1.setClickable(false);
            option2.setClickable(false);
            option3.setClickable(false);
            option4.setClickable(false);
        }else{//0->means to enable
            option1.setClickable(true);
            option2.setClickable(true);
            option3.setClickable(true);
            option4.setClickable(true);
        }
    }
    //50 : 50 Lifeline implementation

    public void LifeLine50_50(View view){
        int correctOptionNumber=getCorrectOptionNumber();
        int randomOptionNum=getRandomOptionNumber(correctOptionNumber);
        if(correctOptionNumber!=1 && randomOptionNum!=1)
            option1.setVisibility(option1.GONE);
        if(correctOptionNumber!=2 && randomOptionNum!=2)
            option2.setVisibility(option2.GONE);
        if(correctOptionNumber!=3 && randomOptionNum!=3)
            option3.setVisibility(option3.GONE);
        if(correctOptionNumber!=4 && randomOptionNum!=4)
            option4.setVisibility(option4.GONE);
    }

    int getCorrectOptionNumber(){
        String correctAns=currentQuestion.getAnswer();
        if(correctAns.equals(currentQuestion.getOption1()))
            return 1;
        else if(correctAns.equals(currentQuestion.getOption2()))
            return 2;
        else if(correctAns.equals(currentQuestion.getOption3()))
            return 3;
        else
            return 4;
    }

    int getRandomOptionNumber(int correctOptionNum){
        int randomOptionNum=-1;
        do{
            randomOptionNum=(int)Math.floor(Math.random()*(4)+1);
        }while (randomOptionNum == correctOptionNum);
        return randomOptionNum;
    }

    /**********************END******************/

    /**************** SWAP QUESTION LIFELINE **************/
    public void swapQuestion(View view){
        countDownTimer.cancel();
        currentQuestion=allQuestions.get(shuffledIndices.get(MAX_QUESTIONS));
        updateAllTheElements();
        isAnyOptionSelected=false;
       // clearOptions();
        StartCountDown();
    }

    /**********************END******************/


    public void audiencePollLifeLine(View view){

        Bundle bundle = new Bundle();

        bundle.putString("correctOption", Integer.toString(getCorrectOptionNumber()) );
        BarChartFragment fragment1 = new BarChartFragment();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment1);
        fragment1.setArguments(bundle);
        transaction.commit();
        /*Need to Implement*/
    }
    public void GetQuestions(String lang,String category){
        String response;
        // Request a string response from the provided URL.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://umairmalik125-001-site1.ctempurl.com/api/QuestionAPI/getQuestions/"+lang+"/"+category;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ALC",response);
                        allQuestions=ConvertJsonToList(response);
                        QuizActivity.this.onResponse();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ALC",error.toString());
                Toast.makeText(QuizActivity.this, "Error Occurred ", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    ArrayList<Question> ConvertJsonToList(String jsonArray) {
        ArrayList<Question> questionArrayList = new ArrayList<Question>();
        Gson gson = new Gson();
        Question[] questionList = gson.fromJson(jsonArray, Question[].class);
        Collections.addAll(questionArrayList, questionList);
        return questionArrayList;
    }
        


}