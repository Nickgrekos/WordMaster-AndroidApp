package com.example.wordmaster;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class TurnActivity extends BaseActivity {

    DatabaseHelper dbHelper;
    String team1Name, team2Name;
    int currentTeamIndex;
    int currentRound;
    int totalRounds = 3;
    int score1 = 0;
    int score2 = 0;

    @Override
    protected void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        refreshUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);

        dbHelper = new DatabaseHelper(this);

        refreshUI();
    }

    private void refreshUI() {
        loadTeamNames();

        currentRound = getIntent().getIntExtra("CURRENT_ROUND", 1);
        score1 = getIntent().getIntExtra("SCORE1", 0);
        score2 = getIntent().getIntExtra("SCORE2", 0);
        currentTeamIndex = getIntent().getIntExtra("CURRENT_TEAM", 0);

        int remainingAtStart = dbHelper.getUnusedWordCount();
        if (remainingAtStart == 0) {
            if (currentRound < totalRounds) {
                currentRound++;
                dbHelper.resetAllWordsToUnused();
            } else {
                Intent intent = new Intent(this, WinnerActivity.class);
                intent.putExtra("SCORE1", score1);
                intent.putExtra("SCORE2", score2);
                intent.putExtra("TEAM1_NAME", team1Name);
                intent.putExtra("TEAM2_NAME", team2Name);
                startActivity(intent);
                finish();
                return;
            }
        }

        TextView tvCurrentTeam = findViewById(R.id.tvCurrentTeam);
        TextView tvRound = findViewById(R.id.tvRound);
        TextView tvGameTitle = findViewById(R.id.tvGameTitle);
        TextView tvWordCount = findViewById(R.id.tvWordCount);
        TextView tvTeam1Name = findViewById(R.id.tvTeam1Name);
        TextView tvTeam2Name = findViewById(R.id.tvTeam2Name);
        TextView tvScore1 = findViewById(R.id.tvScore1);
        TextView tvScore2 = findViewById(R.id.tvScore2);
        Button btnStart = findViewById(R.id.btnStart);

        tvCurrentTeam.setText(currentTeamIndex == 0 ? team1Name : team2Name);
        tvRound.setText(currentRound + " / " + totalRounds);
        String gameTitle;
        switch (currentRound) {
            case 1:
                gameTitle = "Περιγραφή";
                break;
            case 2:
                gameTitle = "Παντομίμα";
                break;
            case 3:
                gameTitle = "Μία Λέξη";
                break;
            default:
                gameTitle = "Παιχνίδι";
        }
        tvGameTitle.setText(gameTitle);

        int remaining = dbHelper.getUnusedWordCount();
        int totalWords = dbHelper.getTotalWordCount();
        tvWordCount.setText(remaining + " / " + totalWords);
        tvTeam1Name.setText(team1Name);
        tvTeam2Name.setText(team2Name);
        tvScore1.setText(String.valueOf(score1));
        tvScore2.setText(String.valueOf(score2));

        btnStart.setOnClickListener(v -> startGameplay());
    }

    private void startGameplay() {
        String currentTeamName = currentTeamIndex == 0 ? team1Name : team2Name;
        int currentTeamId = currentTeamIndex + 1; // Team ID: 1 or 2

        Intent intent = new Intent(this, GameplayActivity.class);
        intent.putExtra("TEAM_NAME", currentTeamName);
        intent.putExtra("TEAM_ID", currentTeamId);
        intent.putExtra("TOTAL_ROUNDS", totalRounds);
        intent.putExtra("CURRENT_ROUND", currentRound);
        intent.putExtra("SCORE1", score1);
        intent.putExtra("SCORE2", score2);
        startActivity(intent);
    }

    private void loadTeamNames() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT team_name FROM teams ORDER BY id ASC", null);

        team1Name = "Ομάδα Α";
        team2Name = "Ομάδα Β";

        if (cursor.moveToFirst()) {
            team1Name = cursor.getString(0);
            if (cursor.moveToNext()) {
                team2Name = cursor.getString(0);
            }
        }
        cursor.close();
        db.close();
    }
}