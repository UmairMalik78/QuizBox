package com.example.quizbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String ENG_QUEST_TABLE = "QuestionPool";
    public static final String URDU_QUEST_TABLE = "QuestionPool";
    public static final String QUESTION_ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY = "category";
    public static final String OPTION_1 = "option1";
    public static final String OPTION_2 = "option2";
    public static final String OPTION_3 = "option3";
    public static final String OPTION_4 = "option4";
    public static final String ANSWER = "answer";
    public static final String LEVEL = "level";

    public static final String IMAGE_PATH = "image_path";

    public DBHelper(@Nullable Context context) {
        super(context, "MyQuestionDB.db", null, 1);
        Log.d("ALC", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("ALC", "Database created");
        //String createTableSTatementOne = "CREATE TABLE CustTable(CustomerID Integer PRIMARY KEY AUTOINCREMENT, " + CUSTOMER_NAME_FIRST + " Text, CustomerAge Int, ActiveCustomer BOOL) ";
        String createTableStatement = "CREATE TABLE " + ENG_QUEST_TABLE
                + "(" + QUESTION_ID + " Integer PRIMARY KEY AUTOINCREMENT,"
                + CATEGORY + " Text, "
                + DESCRIPTION + " Text, "
                + OPTION_1 + " Text, "
                + OPTION_2 + " Text, "
                + OPTION_3 + " Text, "
                + OPTION_4 + " Text, "
                + ANSWER + " Text , "
                + LEVEL + " Text )";
        db.execSQL(createTableStatement);
        createTableStatement = "CREATE TABLE " + URDU_QUEST_TABLE
                + "(" + QUESTION_ID + " Integer PRIMARY KEY AUTOINCREMENT,"
                + CATEGORY + " Text, "
                + DESCRIPTION + " Text, "
                + OPTION_1 + " Text, "
                + OPTION_2 + " Text, "
                + OPTION_3 + " Text, "
                + OPTION_4 + " Text, "
                + ANSWER + " Text , "
                + LEVEL + " Text )";
        db.execSQL(createTableStatement);
    }

    void initializeEnglishQuestionTable() {

    }

    void initializeUrduQuestionTable() {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void addQuestion(ArrayList<Question> questions,String language) {
        String tableName;
        if(language.toLowerCase().equals("urdu"))
            tableName=URDU_QUEST_TABLE;
        else
            tableName=ENG_QUEST_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            //Hash map, as we did in bundles
            ContentValues cv = new ContentValues();
            cv.put(OPTION_1, question.getOption1());
            cv.put(CATEGORY, question.getCategory());
            cv.put(DESCRIPTION, question.getQuestionDescription());
            cv.put(ANSWER, question.getAnswer());
            cv.put(OPTION_3, question.getOption3());
            cv.put(OPTION_4, question.getOption4());
            cv.put(OPTION_2, question.getOption2());
            cv.put(IMAGE_PATH, question.getImg_path());
            //insert data to table
            long insert = db.insert(tableName, null, cv);
        }
        db.close();
    }

    public ArrayList<Question> GetAllQuestions(String questionCategory, String level,String language) {
        String tableName;
        if(language.toLowerCase().equals("urdu"))
            tableName=URDU_QUEST_TABLE;
        else
            tableName=ENG_QUEST_TABLE;

        ArrayList<Question> questionList = new ArrayList<>();
        String query = "Select * from " + tableName + " WHERE " + CATEGORY + "=? AND " + LEVEL + "=?";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{questionCategory, level});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String category = cursor.getString(1);
                String description = cursor.getString(2);
                String option1 = cursor.getString(3);
                String option2 = cursor.getString(4);
                String option3 = cursor.getString(5);
                String option4 = cursor.getString(6);
                String answer = cursor.getString(7);
                Question question = new Question(description, option1, option2, option3, option4, answer);
                questionList.add(question);
            } while (cursor.moveToNext() != false);
        }
        cursor.close();
        sqLiteDatabase.close();
        return questionList;
    }
}