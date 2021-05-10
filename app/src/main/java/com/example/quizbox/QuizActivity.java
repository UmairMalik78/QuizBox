package com.example.quizbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    TextView questionDescription, option1, option2, option3, option4, questionNum, score, levelView;
    int currentQuestionNum=1,quizScore=0;
    Button nextBtn;
    Question currentQuestion;
    boolean isAnyOptionSelected=false;
    int MAX_QUESTIONS=10,correctAnsCount=0;
    ArrayList<Question> allQuestions;
    Boolean[] answersStatusList=new Boolean[MAX_QUESTIONS];
    int[]shuffledIndices;
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
        levelView = findViewById(R.id.levelNoView);
        nextBtn=findViewById(R.id.nextBtn);
        //getting category and levelNo
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        String levelNo = intent.getStringExtra("level");

        //getting all the questions.
        DBHelper dbHelper = new DBHelper(QuizActivity.this);
        allQuestions = dbHelper.GetAllQuestions(category, levelNo);

        //Getting shuffled indices
        shuffledIndices=Question.GetShuffledIndices(0,MAX_QUESTIONS-1);
        currentQuestion=allQuestions.get(shuffledIndices[currentQuestionNum-1]);
        //Setting Elements
        questionNum.setText(String.valueOf(currentQuestionNum));
        levelView.setText(levelNo);
        SetQuestionNumber();
        SetQuestionAndOptions();
        SetScore();
    }

    public void SetScore(){
        score.setText(String.valueOf(quizScore));
    }

    public void SetQuestionNumber(){
        String text="Question Number: "+String.valueOf(currentQuestionNum);
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
        String usersAns=((TextView)view).getText().toString();
        if(usersAns.equals(currentQuestion.getAnswer())){
            correctAnsCount++;
            ((TextView)view).setBackgroundResource(R.color.green);
            quizScore+=10;
            answersStatusList[currentQuestionNum-1]=true;
            isAnyOptionSelected=true;
            SetScore();
        }else{
            ((TextView)view).setBackgroundResource(R.color.red);
            isAnyOptionSelected=true;
            answersStatusList[currentQuestionNum-1]=false;
        }
    }
}