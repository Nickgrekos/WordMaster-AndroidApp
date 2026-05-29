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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);

        dbHelper = new DatabaseHelper(this);

        // Παίρνουμε ονόματα ομάδων από τη βάση
        loadTeamNames();

        // Παίρνουμε γύρο και σκορ από intent (ή αρχικές τιμές)
        currentRound = getIntent().getIntExtra("CURRENT_ROUND", 1);
        score1 = getIntent().getIntExtra("SCORE1", 0);
        score2 = getIntent().getIntExtra("SCORE2", 0);

        // Αν είναι ο πρώτος γύρος, τυχαία επιλογή ομάδας
        if (currentRound == 1) {
            currentTeamIndex = new Random().nextInt(2);
        } else {
            currentTeamIndex = getIntent().getIntExtra("CURRENT_TEAM", 0);
        }

        TextView tvCurrentTeam = findViewById(R.id.tvCurrentTeam);
        TextView tvRound = findViewById(R.id.tvRound);
        TextView tvTeam1Name = findViewById(R.id.tvTeam1Name);
        TextView tvTeam2Name = findViewById(R.id.tvTeam2Name);
        TextView tvScore1 = findViewById(R.id.tvScore1);
        TextView tvScore2 = findViewById(R.id.tvScore2);
        Button btnStart = findViewById(R.id.btnStart);

        // Εμφάνιση δεδομένων
        tvCurrentTeam.setText(currentTeamIndex == 0 ? team1Name : team2Name);
        tvRound.setText(currentRound + " / " + totalRounds);
        tvTeam1Name.setText(team1Name);
        tvTeam2Name.setText(team2Name);
        tvScore1.setText(String.valueOf(score1));
        tvScore2.setText(String.valueOf(score2));

        btnStart.setOnClickListener(v -> {
            // εδώ θα πας στην οθόνη παιχνιδιού
        });
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