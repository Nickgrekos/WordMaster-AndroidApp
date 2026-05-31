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

        tvTeamName.setText(currentTeamName);

        loadNextWord();

        btnSkip.setOnClickListener(v -> skipWord());
        btnTick.setOnClickListener(v -> correctWord());

        startGameTimer();
    }

    private void loadNextWord() {
        currentWord = dbHelper.getRandomUnusedWord();
        if (currentWord != null) {
            tvWord.setText(currentWord);
        } else {
            endTurn();
        }
    }

    private void skipWord() {
        if (currentWord != null) {
            loadNextWord();
        }
    }

    private void correctWord() {
        if (currentWord != null) {
            dbHelper.markWordAsUsed(currentWord);

            int newScore = (currentTeamId == 1) ? score1 + 1 : score2 + 1;
            if (currentTeamId == 1) {
                score1 = newScore;
            } else {
                score2 = newScore;
            }
            dbHelper.updateTeamScore(currentTeamId, newScore);
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
        if (gameTimer != null) {
            gameTimer.cancel();
        }

        btnSkip.setEnabled(false);
        btnTick.setEnabled(false);

        Intent intent = new Intent(this, TurnActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("CURRENT_ROUND", currentRound);
        intent.putExtra("SCORE1", score1);
        intent.putExtra("SCORE2", score2);
        intent.putExtra("CURRENT_TEAM", currentTeamId == 1 ? 1 : 0); // Switch to the other team
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

