package com.example.quizbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.opengl.Visibility;
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
import android.widget.LinearLayout;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphTextView;

public class QuizActivity extends AppCompatActivity implements OnResponse {
    MediaPlayer optionClickMediaPlayer,timerMediaPlayer;
    TextView questionDescription, questionNum, score,timerTextView;
    NeumorphButton option1,option2, option3, option4;
    int currentQuestionNum=1,quizScore=0;
    NeumorphButton closeBtn;
    Button nextBtn,lifeline50_50Btn,lifelineAudiencePollBtn,lifelineSwapBtn;
    Question currentQuestion;
    boolean isAnyOptionSelected=false;
    int MAX_QUESTIONS=10,correctAnsCount=0;
    ArrayList<Question> allQuestions,currentLevelQuestions;
    Boolean[] answersStatusList=new Boolean[MAX_QUESTIONS];
    ArrayList<Integer>shuffledIndices;
    ProgressBar progressBar;
    CountDownTimer countDownTimer;
    int timer=0,currentLevelNo=1,currentLevelStartingIndex=0;
    FrameLayout frameLayout;
    boolean isReleased=false;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("value",timer);
    }

    int MAX_TIME_IN_MILLIS=20000;
    int COUNT_DOWN_INTERVAL_IN_MILLIS=1000;
    String correctOptionView;
    int clickDisableFlag=1;
    final int TOTAL_LEVELS=3;
    final int QUESTION_IN_EACH_LEVEL=5;
    ProgressBar loader;
    LinearLayout optionsLayout,lifeLineLayout;
    boolean isAudiencePollUsed=false,isFiftyFiftyUsed=false;

    @Override
    public void onResponse() {
        //Shuffling Questions
        Collections.shuffle(allQuestions);
        //Setting First Question
        currentQuestion=allQuestions.get(currentQuestionNum-1);
        //Setting Elements
        SetQuestionNumber();
        SetQuestionAndOptions();
        SetScore();
        ShowHidedElements();
        playTimerSound();
        StartCountDown();
        ToggleClickFunctionalityOfOptionsViews();
        loader.setVisibility(View.GONE);
        PerformAnimations();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //getting all the elements
        lifeLineLayout=findViewById(R.id.linearLayoutForLifelines);
        optionsLayout=findViewById(R.id.linearLayoutForOptions);
        closeBtn=findViewById(R.id.closeBtn);
        questionDescription = findViewById(R.id.questionDescription);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        score = findViewById(R.id.scoreView);
        frameLayout=findViewById(R.id.frameLayout);
        questionNum = findViewById(R.id.questionNumView);
        progressBar=findViewById(R.id.progress_bar);
        progressBar.setProgress(timer);
        timerTextView=findViewById(R.id.counter);
        loader=findViewById(R.id.loader);
        // TextView textView=findViewById(R.id.myView);
        // getting category and lang from previous activity
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        String lang = intent.getStringExtra("lang");

        //Toast msg to show loading
        Toast.makeText(this,"Wait while questions loading",Toast.LENGTH_LONG).show();
        //Hiding elements until question load into memory
        HideElements();

        // getting all the questions from API.
        GetQuestions(lang,category);

        //Checking if onSaveInstanceState contains old timer valye or not?
        if(savedInstanceState!=null){
            timer=savedInstanceState.getInt("value");
        }
    }

    private void PerformAnimations(){
        option1.setTranslationY(300);
        option2.setTranslationY(300);
        option3.setTranslationY(300);
        option4.setTranslationY(300);
        questionDescription.setTranslationX(300);

        option1.setAlpha(0);
        option2.setAlpha(0);
        option3.setAlpha(0);
        option4.setAlpha(0);
        questionDescription.setAlpha(0);

        option1.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        option2.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        option3.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        option4.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        questionDescription.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
    }

    private void HideElements() {
        lifeLineLayout.setVisibility(View.GONE);
        optionsLayout.setVisibility(View.GONE);
        questionDescription.setVisibility(View.GONE);
    }
    private void ShowHidedElements() {
        lifeLineLayout.setVisibility(View.VISIBLE);
        optionsLayout.setVisibility(View.VISIBLE);
        questionDescription.setVisibility(View.VISIBLE);
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
        (option1).setBackgroundColor(getResources().getColor(R.color.white));
        (option2).setBackgroundColor(getResources().getColor(R.color.white));
        (option3).setBackgroundColor(getResources().getColor(R.color.white));
        (option4).setBackgroundColor(getResources().getColor(R.color.white));

    }

    public void updateAllTheElements(){
        SetQuestionAndOptions();
        SetQuestionNumber();
    }


    public void SetNextQuestionOnScreen(){
        PerformAnimations();
        Log.d("ALC","Timer Sound");
        playTimerSound();
        StartCountDown();
        CloseFragment(closeBtn);
        currentQuestionNum=currentQuestionNum+1;
        if(currentQuestionNum==MAX_QUESTIONS){//checks if last question has come?
          //  String text="Finish Quiz";
           // nextBtn.setText(text);
        }else if(currentQuestionNum>MAX_QUESTIONS){
            timerMediaPlayer.reset();
            timerMediaPlayer.release();
            countDownTimer.cancel();
            MoveToResultActivity();
            return;
        }
        currentQuestion=allQuestions.get(currentQuestionNum-1);
        updateAllTheElements();
        isAnyOptionSelected=false;
        SetAllOptionsVisible();
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
            ((NeumorphButton)view).setBackgroundColor(getResources().getColor(R.color.green ));;
            correctAnsCount++;
            quizScore+=10;
            answersStatusList[currentQuestionNum-1]=true;
            isAnyOptionSelected=true;
            SetScore();
        }else{
            ((NeumorphButton)view).setBackgroundColor(getResources().getColor(R.color.red));
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
        if(isFiftyFiftyUsed){
            Toast.makeText(this,"Lifeline already used!",Toast.LENGTH_LONG).show();
            return;
        }
        isFiftyFiftyUsed=true;
        int correctOptionNumber=getCorrectOptionNumber();
        int randomOptionNum=getRandomOptionNumber(correctOptionNumber);
        if(correctOptionNumber!=1 && randomOptionNum!=1)
            HideOptionWithAnimation(option1);
        if(correctOptionNumber!=2 && randomOptionNum!=2)
            HideOptionWithAnimation(option2);
        if(correctOptionNumber!=3 && randomOptionNum!=3)
            HideOptionWithAnimation(option3);
        if(correctOptionNumber!=4 && randomOptionNum!=4)
            HideOptionWithAnimation(option4);
    }
    private void HideOptionWithAnimation(View view){
        ((NeumorphButton)view).setAlpha(1);
        ((NeumorphButton)view).animate().alpha(0).setDuration(1000).setStartDelay(400).start();
    }
    //TO display again after next question
    void SetAllOptionsVisible(){
        option1.setVisibility(option1.VISIBLE);
        option2.setVisibility(option2.VISIBLE);
        option3.setVisibility(option3.VISIBLE);
        option4.setVisibility(option4.VISIBLE);
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


    public void audiencePollLifeLine(View view){
        if(isAudiencePollUsed){
            Toast.makeText(this,"Lifeline already used!",Toast.LENGTH_LONG).show();
            return;
        }
        isAudiencePollUsed=true;
        Bundle bundle = new Bundle();
        bundle.putString("correctOption", Integer.toString(getCorrectOptionNumber()) );
        BarChartFragment fragment1 = new BarChartFragment();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment1);
        fragment1.setArguments(bundle);
        //before display fragment, visible close btn to get back from fragment
        AudiencePollOnAnimation();
        transaction.commit();
        closeBtn.setVisibility(View.VISIBLE);
        /*Need to Implement*/
    }
    private void AudiencePollOnAnimation(){
        frameLayout.setTranslationY(3000);
        frameLayout.setAlpha(0);
        frameLayout.animate().alpha(1).translationY(0).setDuration(1000).setStartDelay(0).start();

    }
    private void AudiencePollCloseAnimation(){
        frameLayout.setAlpha(1);
        frameLayout.animate().alpha(0).setDuration(1000).setStartDelay(0).start();
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

    public void CloseFragment(View view){
        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frameLayout);
        if(fragment!=null){

            fragmentManager.beginTransaction();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            AudiencePollCloseAnimation();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
            ((Button)view).setVisibility(View.GONE);
        }
    }
    private void playTimerSound(){
        if(timerMediaPlayer!=null && !isReleased)
        {
            if(timerMediaPlayer.isPlaying()){
                timerMediaPlayer.release();
                isReleased=true;
                timerMediaPlayer=null;
            }
        }
        timerMediaPlayer=MediaPlayer.create(QuizActivity.this,R.raw.tick_tock_bell);
        timerMediaPlayer.start();
        timerMediaPlayer.setLooping(false);
        isReleased=false;
        timerMediaPlayer.setOnCompletionListener(new  MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                timerMediaPlayer.reset();
                timerMediaPlayer.release();
                isReleased=true;
                timerMediaPlayer=null;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        stopTimerSound();
        countDownTimer.cancel();
    }
    private void stopTimerSound(){
        if(timerMediaPlayer!=null && !isReleased)
        {
            if(timerMediaPlayer.isPlaying()){
                timerMediaPlayer.release();
                isReleased=true;
                timerMediaPlayer=null;
            }
        }
    }
}