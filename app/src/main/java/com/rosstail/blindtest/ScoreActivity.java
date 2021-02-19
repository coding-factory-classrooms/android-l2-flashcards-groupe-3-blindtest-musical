package com.rosstail.blindtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent srcIntent = getIntent();
        int score = srcIntent.getIntExtra("score", 0);
        int nbOfQuestions = srcIntent.getIntExtra("numberOfQuestions", 0);
        String level = srcIntent.getStringExtra("level");

        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(score + " / " + nbOfQuestions + " -> " + ((int) ((float) score / (float) nbOfQuestions * 100)) + "%");

        TextView levelTextView = findViewById(R.id.levelTextView);
        levelTextView.setText("Niveau " + level);

    }
}