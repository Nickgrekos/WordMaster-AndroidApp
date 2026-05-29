package com.example.wordmaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wordmaster.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_WORDS = "words";
    public static final String COL_ID = "id";
    public static final String COL_PLAYER = "player_number";
    public static final String COL_WORD = "word";
    public static final String COL_USED = "is_used";

    public static final String TABLE_TEAMS = "teams";
    public static final String COL_TEAM_ID = "id";
    public static final String COL_TEAM_NAME = "team_name";
    public static final String COL_TEAM_SCORE = "score";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_WORDS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PLAYER + " INTEGER, " +
                COL_WORD + " TEXT, " +
                COL_USED + " INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE " + TABLE_TEAMS + " (" +
                COL_TEAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TEAM_NAME + " TEXT, " +
                COL_TEAM_SCORE + " INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
        onCreate(db);
    }

    public void insertWord(int playerNumber, String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PLAYER, playerNumber);
        values.put(COL_WORD, word);
        values.put(COL_USED, 0);
        db.insert(TABLE_WORDS, null, values);
        db.close();
    }

    public void clearAllWords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_WORDS);
        db.close();
    }

    public void insertTeam(String teamName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TEAM_NAME, teamName);
        values.put(COL_TEAM_SCORE, 0);
        db.insert(TABLE_TEAMS, null, values);
        db.close();
    }

    public void clearAllTeams() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TEAMS);
        db.close();
    }

    public void resetGame() {
        clearAllWords();
        clearAllTeams();
    }
}