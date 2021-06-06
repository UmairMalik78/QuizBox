package com.example.quizbox;

import android.util.Log;

import java.util.ArrayList;

public class MyMath {
    static public ArrayList<Integer> GetShuffleIndices(int noOfLevel, int interval){
        int totalQuestions=interval*noOfLevel;
        ArrayList<Integer> shuffledIndices=new ArrayList<Integer>();
        int startIndex=0;
        for(int i=1;i<=noOfLevel;i++){
            shuffledIndices.addAll(GetRandomNumbers(startIndex,i*interval-1));
            startIndex=startIndex+interval;
        }
        return shuffledIndices;
    }
    //Get random sequence between specified range
    static public ArrayList<Integer> GetRandomNumbers(int min,int max){
        int valuesCount=max-min+1;
        int[] originalSequencedArray=new int[valuesCount];
        ArrayList<Integer> indicesArray=new ArrayList<Integer>();
        for(int i=min;i<=max;i++){originalSequencedArray[i-min]=i;}
        for (int i = 0; i < valuesCount; i++) {
            int randomIndex=(int)Math.floor(Math.random()*(valuesCount-i)+i);
            indicesArray.add(i,originalSequencedArray[randomIndex]);
            originalSequencedArray[randomIndex]=originalSequencedArray[i];
            min++;
        }
        return indicesArray;
    }
}
