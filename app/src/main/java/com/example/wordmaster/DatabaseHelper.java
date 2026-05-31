package com.example.wordmaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public String getRandomUnusedWord() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_WORDS,
                new String[]{COL_WORD},
                COL_USED + " = 0",
                null,
                null,
                null,
                "RANDOM()",
                "1"
        );

        String word = null;
        if (cursor.moveToFirst()) {
            word = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return word;
    }

    public void markWordAsUsed(String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        String subQuery = "SELECT " + COL_ID + " FROM " + TABLE_WORDS + " WHERE " + COL_WORD + " = ? AND " + COL_USED + " = 0 LIMIT 1";
        db.execSQL("UPDATE " + TABLE_WORDS + " SET " + COL_USED + " = 1 WHERE " + COL_ID + " = (" + subQuery + ")", new String[]{word});
        db.close();
    }

    public void resetAllWordsToUnused() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USED, 0);
        db.update(TABLE_WORDS, values, null, null);
        db.close();
    }

    public int getUnusedWordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_WORDS + " WHERE " + COL_USED + " = 0", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public void updateTeamScore(int teamId, int newScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TEAM_SCORE, newScore);
        db.update(TABLE_TEAMS, values, COL_TEAM_ID + " = ?", new String[]{String.valueOf(teamId)});
        db.close();
    }

    public int getTeamScore(int teamId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_TEAMS,
                new String[]{COL_TEAM_SCORE},
                COL_TEAM_ID + " = ?",
                new String[]{String.valueOf(teamId)},
                null,
                null,
                null
        );
        int score = 0;
        if (cursor.moveToFirst()) {
            score = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return score;
    }

    public java.util.List<String> getAllTeams() {
        java.util.List<String> teams = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT team_name FROM " + TABLE_TEAMS + " ORDER BY id ASC", null);
        if (cursor.moveToFirst()) {
            do {
                teams.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return teams;
    }

    public int getTotalWordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_WORDS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }
}