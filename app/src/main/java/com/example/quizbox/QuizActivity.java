package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    TextView questionDescription, option1, option2, option3, option4, questionNum, score,timerTextView;
    int currentQuestionNum=1,quizScore=0;
    Button nextBtn,lifeline50_50Btn,lifelineAudiencePollBtn,lifelineSwapBtn;
    Question currentQuestion;
    boolean isAnyOptionSelected=false;
    int MAX_QUESTIONS=10,correctAnsCount=0;
    ArrayList<Question> allQuestions;
    Boolean[] answersStatusList=new Boolean[MAX_QUESTIONS];
    int[]shuffledIndices;
    ProgressBar progressBar;
    CountDownTimer countDownTimer;
    int timer=0;
    int MAX_TIME_IN_MILLIS=20000;
    int COUNT_DOWN_INTERVAL_IN_MILLIS=1000;
    String correctOptionView;

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
        lifeline50_50Btn=findViewById(R.id.lifeline50_50);
        lifelineAudiencePollBtn=findViewById(R.id.lifelineAudiencePoll);
        lifelineSwapBtn=findViewById(R.id.lifelineSwap);
        progressBar=findViewById(R.id.progress_bar);
        progressBar.setProgress(timer);
        timerTextView=findViewById(R.id.counter);
        TextView textView=findViewById(R.id.myView);

        //getting category and levelNo from previous activity
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        //getting all the questions.
        DBHelper dbHelper = new DBHelper(QuizActivity.this);
        allQuestions = dbHelper.GetAllQuestions(category,"english");

        Log.e("ALC","Size of Array: "+String.valueOf(allQuestions.size()));
        //Getting shuffled indices
        shuffledIndices=Question.GetShuffledIndices(0,MAX_QUESTIONS-1);

        currentQuestion=allQuestions.get(shuffledIndices[currentQuestionNum-1]);
        //Setting Elements
        SetQuestionNumber();
        SetQuestionAndOptions();
        SetScore();
        StartCountDown();

    }

    public void StartCountDown() {
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
            }
        }.start();
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
    }

    public void updateAllTheElements(){
        SetQuestionAndOptions();
        SetQuestionNumber();
    }

    public void SetNextQuestionOnScreen(View view){
        if(isAnyOptionSelected==false){
            Toast.makeText(QuizActivity.this,"Please Select atleast one option to proceed",Toast.LENGTH_SHORT).show();
            return;
        }
        currentQuestionNum=currentQuestionNum+1;
        if(currentQuestionNum==MAX_QUESTIONS){//checks if last question has come?
            String text="Finish Quiz";
            nextBtn.setText(text);
        }else if(currentQuestionNum>MAX_QUESTIONS){
            MoveToResultActivity();
            return;
        }
        currentQuestion=allQuestions.get(shuffledIndices[currentQuestionNum-1]);
        updateAllTheElements();
        isAnyOptionSelected=false;
        clearOptions();
    }
    public void clearOptions(){
        option1.setBackgroundResource(R.color.white);
        option2.setBackgroundResource(R.color.white);
        option3.setBackgroundResource(R.color.white);
        option4.setBackgroundResource(R.color.white);
    }

    private void MoveToResultActivity() {
        Intent intent=new Intent(this,ResultActivity.class);
        intent.putExtra("answerStatusList",answersStatusList);
        intent.putExtra("correctAnsCount",correctAnsCount);
        startActivity(intent);
    }
    public void CheckAnswer(View view){
        if(isAnyOptionSelected){
            Toast.makeText(QuizActivity.this,"Sorry You cannot select more than one options",Toast.LENGTH_SHORT).show();
            return;
        }
        countDownTimer.cancel();
        String usersAns=((TextView)view).getText().toString();
        Drawable background=((TextView)view).getBackground();
        ShapeDrawable shapeDrawable = (ShapeDrawable) background;
        if(usersAns.equals(currentQuestion.getAnswer())){
            ((TextView)view).getBackground().setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.SRC_ATOP);
            shapeDrawable.getPaint().setColor(ContextCompat.getColor(this,R.color.green));
            correctAnsCount++;
            quizScore+=10;
            answersStatusList[currentQuestionNum-1]=true;
            isAnyOptionSelected=true;
            SetScore();
        }else{
            ((TextView)view).getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_ATOP);
            isAnyOptionSelected=true;
            answersStatusList[currentQuestionNum-1]=false;
            shapeDrawable.getPaint().setColor(ContextCompat.getColor(this,R.color.red));
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
        currentQuestion=allQuestions.get(shuffledIndices[10]);
        updateAllTheElements();
        isAnyOptionSelected=false;
        clearOptions();
        countDownTimer.cancel();
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
}