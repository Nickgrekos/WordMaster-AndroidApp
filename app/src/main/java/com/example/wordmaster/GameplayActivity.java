package com.example.wordmaster;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameplayActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String currentTeamName;
    private int currentTeamId;
    private String currentWord;
    private CountDownTimer gameTimer;
    private int totalRounds;
    private int currentRound;
    private int score1;
    private int score2;

    private TextView tvTeamName;
    private TextView tvTimer;
    private TextView tvWord;
    private Button btnSkip;
    private Button btnTick;

    private static final long GAME_DURATION = 10000;//60000; // 1 minute in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        dbHelper = new DatabaseHelper(this);

        // Get data from intent
        currentTeamName = getIntent().getStringExtra("TEAM_NAME");
        currentTeamId = getIntent().getIntExtra("TEAM_ID", 1);
        totalRounds = getIntent().getIntExtra("TOTAL_ROUNDS", 3);
        currentRound = getIntent().getIntExtra("CURRENT_ROUND", 1);
        score1 = getIntent().getIntExtra("SCORE1", 0);
        score2 = getIntent().getIntExtra("SCORE2", 0);

        // Initialize views
        tvTeamName = findViewById(R.id.tvTeamName);
        tvTimer = findViewById(R.id.tvTimer);
        tvWord = findViewById(R.id.tvWord);
        btnSkip = findViewById(R.id.btnSkip);
        btnTick = findViewById(R.id.btnTick);

        // Set team name
        tvTeamName.setText(currentTeamName);

        // Load first word
        loadNextWord();

        // Set up button listeners
        btnSkip.setOnClickListener(v -> skipWord());
        btnTick.setOnClickListener(v -> correctWord());

        // Start timer
        startGameTimer();
    }

    private void loadNextWord() {
        currentWord = dbHelper.getRandomUnusedWord();
        if (currentWord != null) {
            tvWord.setText(currentWord);
        } else {
            // No more words available
            endTurn();
        }
    }

    private void skipWord() {
        if (currentWord != null) {
            // Word is recycled (kept in unused pool), just load next word
            loadNextWord();
        }
    }

    private void correctWord() {
        if (currentWord != null) {
            // Mark word as used
            dbHelper.markWordAsUsed(currentWord);

            // Update score
            int newScore = (currentTeamId == 1) ? score1 + 1 : score2 + 1;
            if (currentTeamId == 1) {
                score1 = newScore;
            } else {
                score2 = newScore;
            }
            dbHelper.updateTeamScore(currentTeamId, newScore);

            // Load next word
            loadNextWord();
        }
    }

    private void startGameTimer() {
        gameTimer = new CountDownTimer(GAME_DURATION, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                tvTimer.setText(String.format("00:%02d", secondsRemaining));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                endTurn();
            }
        };
        gameTimer.start();
    }

    private void endTurn() {
        // Cancel timer if still running
        if (gameTimer != null) {
            gameTimer.cancel();
        }

        // Disable buttons
        btnSkip.setEnabled(false);
        btnTick.setEnabled(false);

        // Reset word pool for next turn
        //dbHelper.resetAllWordsToUnused();

        // Navigate to TurnActivity or EndGameActivity
        Intent intent = new Intent(this, TurnActivity.class);
        intent.putExtra("CURRENT_ROUND", currentRound);
        intent.putExtra("SCORE1", score1);
        intent.putExtra("SCORE2", score2);
        intent.putExtra("CURRENT_TEAM", currentTeamId == 1 ? 0 : 1);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }
}

