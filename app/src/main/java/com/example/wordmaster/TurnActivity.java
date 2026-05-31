package com.example.wordmaster;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class TurnActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    String team1Name, team2Name;
    int currentTeamIndex; // 0 = ομάδα1, 1 = ομάδα2
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
        // Παίρνουμε ονόματα ομάδων από τη βάση
        loadTeamNames();

        // Παίρνουμε γύρο και σκορ από intent (ή αρχικές τιμές)
        currentRound = getIntent().getIntExtra("CURRENT_ROUND", 1);
        score1 = getIntent().getIntExtra("SCORE1", 0);
        score2 = getIntent().getIntExtra("SCORE2", 0);
        currentTeamIndex = getIntent().getIntExtra("CURRENT_TEAM", 0);

        // Check if all words are used to advance the round
        int remainingAtStart = dbHelper.getUnusedWordCount();
        if (remainingAtStart == 0) {
            if (currentRound < totalRounds) {
                currentRound++;
                dbHelper.resetAllWordsToUnused();
            } else {
                // Game finished logic could go here
                android.widget.Toast.makeText(this, "Το παιχνίδι τελείωσε!", android.widget.Toast.LENGTH_LONG).show();
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

        // Εμφάνιση δεδομένων
        tvCurrentTeam.setText(currentTeamIndex == 0 ? team1Name : team2Name);
        tvRound.setText(currentRound + " / " + totalRounds);
        // Set game title based on which round we're on
        String gameTitle;
        switch (currentRound) {
            case 1:
                gameTitle = "Περιγραφή"; // description round
                break;
            case 2:
                gameTitle = "Παντομίμα"; // pantomime
                break;
            case 3:
                gameTitle = "Μία Λέξη"; // one word
                break;
            default:
                gameTitle = "Παιχνίδι";
        }
        tvGameTitle.setText(gameTitle);

        // Word counter: remaining (unused) / total words
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