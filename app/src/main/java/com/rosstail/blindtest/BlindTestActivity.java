package com.rosstail.blindtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class BlindTestActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mp;

    SongList songList;
    AnswerList answerList;
    int numberTitles;
    int numberAnswers;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind_test);

        findViewById(R.id.playerButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);

        RadioGroup group = findViewById(R.id.radioGroup);

        Intent srcIntent = getIntent();
        int difficultyChoice = srcIntent.getIntExtra("difficulty", 1);
        songList = srcIntent.getParcelableExtra("songs");
        answerList = srcIntent.getParcelableExtra("answers");
        numberTitles = srcIntent.getIntExtra("titleNumber", 0);
        numberAnswers = srcIntent.getIntExtra("answerNumber", 0);
        score = srcIntent.getIntExtra("score", 0);

        Log.e("BLIND TEST ACTIVITY", difficultyChoice + "");
        ArrayList<SongData> songs = songList.songs;

        int index = getRandomNumber(0, songs.size());
        SongData answerSong = songs.get(index);
        String artist = answerSong.artist;
        String fileName = answerSong.fileName;
        songs.remove(index);
        Log.e("blindactivitytest", artist + "");

        if (artist == null || fileName == null) {
            finish();
            return;
        }

        // Create Media Player //

        Log.e("player activity", fileName);
        String playerFileName = fileName.replace(".mp3", "");
        Log.d("test123", playerFileName);
        createPlayer(playerFileName);

        // ------------------- //

        addAnswers(group, artist, getIntent().getIntExtra("answerNumber", 2));
        setConfirm(group, artist);
    }

    private void setConfirm(RadioGroup group, String artist) {
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            int checkedID = group.getCheckedRadioButtonId();
            if (checkedID != -1) {
                RadioButton radioButton = (RadioButton) findViewById(checkedID);
                TextView label = findViewById(R.id.responseTextView);
                if (radioButton.getText().toString().equalsIgnoreCase(artist)) {
                    score++;
                    label.setText("Right answer ! " + score);
                } else {
                    label.setText("Too bad ! The correct answer is " + artist + " : " + score);
                }
                setNextToConfirm(confirmButton);
            }
        });
    }

    private void setNextToConfirm(Button confirmButton) {
        confirmButton.setText("Next");
        confirmButton.setOnClickListener(b -> {
            Log.i("Next page", "TEST");
        });
    }

    private void addAnswers(RadioGroup group, String artist, int nbAnswer) {
        ArrayList<String> answers = (ArrayList<String>) answerList.answers.clone();

        ArrayList<String> list = new ArrayList<>();

        int len = answers.size();
        for (int i=0;i<len;i++){
            list.add(answers.get(i));
        }

        int rightAnswerIndex = getRandomNumber(0, nbAnswer);
        list.remove(artist);
        for(int i = 0; i < nbAnswer; i++) {
            RadioButton button = new RadioButton(this);
            if (i != rightAnswerIndex) {
                String answer = list.get(getRandomNumber(0, list.size()));
                button.setText(answer);
                list.remove(answer);
            } else {
                button.setText(artist);
            }
            group.addView(button);
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void createPlayer(String fileName){
        int fileId = getResources().getIdentifier(fileName, "raw", getPackageName());
        Log.e("blindactivity", fileId+"");
        mp = MediaPlayer.create(this, fileId);
        //mp.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.playerButton :
                Log.e("blindactivity", "play test");
                mp.start();
                break;
            case R.id.stopButton :
                Log.e("blindactivity", "stop test");
                mp.pause();
                break;
        }
    }
}