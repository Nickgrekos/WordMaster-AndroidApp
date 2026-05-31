package com.example.wordmaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        int score1 = getIntent().getIntExtra("SCORE1", 0);
        int score2 = getIntent().getIntExtra("SCORE2", 0);
        String team1Name = getIntent().getStringExtra("TEAM1_NAME");
        String team2Name = getIntent().getStringExtra("TEAM2_NAME");

        TextView tvWinnerName = findViewById(R.id.tvWinnerName);
        TextView tvTeam1Name = findViewById(R.id.tvTeam1Name);
        TextView tvTeam2Name = findViewById(R.id.tvTeam2Name);
        TextView tvFinalScore1 = findViewById(R.id.tvFinalScore1);
        TextView tvFinalScore2 = findViewById(R.id.tvFinalScore2);
        TextView tvTrophyEmoji = findViewById(R.id.tvTrophyEmoji);
        Button btnBackHome = findViewById(R.id.btnBackHome);
        Button btnPlayAgain = findViewById(R.id.btnPlayAgain);

        tvTeam1Name.setText(team1Name);
        tvTeam2Name.setText(team2Name);
        tvFinalScore1.setText(String.valueOf(score1));
        tvFinalScore2.setText(String.valueOf(score2));

        if (score1 > score2) {
            tvWinnerName.setText(team1Name);
            tvWinnerName.setTextColor(getColor(android.R.color.holo_blue_dark));
            tvTrophyEmoji.setText("🏆");
        } else if (score2 > score1) {
            tvWinnerName.setText(team2Name);
            tvWinnerName.setTextColor(getColor(android.R.color.holo_red_dark));
            tvTrophyEmoji.setText("🏆");
        } else {
            tvWinnerName.setText("Ισοπαλία!");
            tvWinnerName.setTextColor(getColor(android.R.color.darker_gray));
            tvTrophyEmoji.setText("🤝");
        }

        btnBackHome.setOnClickListener(v -> {
            new DatabaseHelper(this).resetGame();
            Intent intent = new Intent(WinnerActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnPlayAgain.setOnClickListener(v -> {
            new DatabaseHelper(this).resetGame();
            Intent intent = new Intent(WinnerActivity.this, GameActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}