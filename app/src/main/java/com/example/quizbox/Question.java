
package com.example.quizbox;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Question {
    public String QuestionDescription;
    private String category;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;
    private String level;

    public String getImg_path() {
        return level;
    }

    public void setImg_path(String level) {
        this.level = level;
    }


    public String getQuestionDescription() {
        return QuestionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        QuestionDescription = questionDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Question(String questionDescription, String option1, String option2, String option3, String option4, String answer) {
        QuestionDescription = questionDescription;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
    }

/*
    static public ArrayList<Question> ShuffleQuestions(ArrayList<Question> questionsList) {
        int currentIndex = 0, totalQuestions = questionsList.size();
        Log.e("ALC","Shuffling");
        for (int i = 0; i < totalQuestions; i++) {
            int randomIndex = (int)Math.floor(Math.random()*(totalQuestions-currentIndex)+currentIndex);
            Question temp=questionsList.get(randomIndex);
     		questionsList.set(randomIndex,questionsList.get(currentIndex));
     		questionsList.set(currentIndex,temp);
     		currentIndex++;
        }
        return questionsList;
    }
*/
    static public int[] GetShuffledIndices(int min,int max){
        int valuesCount=max-min+1;
        int[] originalSequencedArray=new int[valuesCount];
        int[] indicesArray=new int[valuesCount];
        for(int i=0;i<valuesCount;i++){originalSequencedArray[i]=i;}
        for (min = 0; min < valuesCount; min++) {
            int randomIndex=(int)Math.floor(Math.random()*(max-min+1)+min);
            indicesArray[min]= originalSequencedArray[randomIndex];
            originalSequencedArray[randomIndex]=originalSequencedArray[min];
        }
        for(int i=0;i<indicesArray.length;i++){
            Log.e("Random",String.valueOf(indicesArray[i]));
        }
        return indicesArray;
    }
}
