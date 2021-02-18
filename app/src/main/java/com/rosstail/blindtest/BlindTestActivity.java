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

import java.util.ArrayList;

public class BlindTestActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mp;

    String level;
    SongList songList;
    ArrayList<SongData> songDatas;
    SongData rightAnswer;
    AnswerList answerList;
    int difficultyChoice;
    int numberTitles;
    int numberAnswers;
    int numberOfQuestions;
    int score;
    int indexPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind_test);

        findViewById(R.id.playerButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);

        RadioGroup group = findViewById(R.id.radioGroup);

        Intent srcIntent = getIntent();
        difficultyChoice = srcIntent.getIntExtra("difficulty", 1);
        songList = srcIntent.getParcelableExtra("songs");

        // Set the number of pages
        songDatas = (ArrayList<SongData>) songList.songs.clone();
        TextView offPage = findViewById(R.id.offPageTextView);
        offPage.setText("/ " + songDatas.size());
        //numberAnswers = songs.size();
        numberOfQuestions = songDatas.size();
        answerList = srcIntent.getParcelableExtra("answers");
        /*numberTitles = srcIntent.getIntExtra("titleNumber", 0);
        numberAnswers = srcIntent.getIntExtra("answerNumber", 0);
        score = srcIntent.getIntExtra("score", 0);*/

        Log.e("BLIND TEST ACTIVITY", difficultyChoice + "");


        int index = getRandomNumber(0, songDatas.size());
        rightAnswer = songDatas.get(index);

        displaySong(rightAnswer, group);

    }

    private void displaySong(SongData songData, RadioGroup group){
        setNumberOfPage();

        String artist = songData.artist;
        String fileName = songData.fileName;
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

        addAnswers(group, artist);
        setConfirm(group, artist);
    }

    private void setConfirm(RadioGroup group, String artist) {
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setText("Confirmer");
        confirmButton.setOnClickListener(v -> {
            int checkedID = group.getCheckedRadioButtonId();
            if (checkedID != -1) {
                RadioButton radioButton = (RadioButton) findViewById(checkedID);
                TextView label = findViewById(R.id.responseTextView);
                if (radioButton.getText().toString().equalsIgnoreCase(artist)) {
                    score++;
                    label.setText("Bonne réponse ! " + score);
                } else {
                    label.setText("Nul ! La bonne réponse était " + artist + " : " + score);
                }
                setNextToConfirm(confirmButton, group);
            }
        });
    }

    private void setNextToConfirm(Button confirmButton, RadioGroup group) {
        confirmButton.setText("Next");
        confirmButton.setOnClickListener(b -> {
            songDatas.remove(rightAnswer);
            if (songDatas.size() <= 0 || indexPage == numberOfQuestions) {
                Log.e("Game Over", score + " / " + numberOfQuestions);
                finish();
            } else {
                indexPage++;
                int index = getRandomNumber(0, songDatas.size());
                rightAnswer = songDatas.get(index);
                mp.stop();
                setConfirm(group, rightAnswer.getArtist());
                group.clearCheck();
                group.removeAllViews();

                TextView label = findViewById(R.id.responseTextView);
                label.setText("Veuillez sélectionner une réponse. ");
                displaySong(rightAnswer, group);
            }
            Log.i("Next page", "TEST");
        });
    }

    private void addAnswers(RadioGroup group, String artist) {
        ArrayList<String> answers = (ArrayList<String>) answerList.answers.clone();
        ArrayList<String> list = new ArrayList<>();

        int nbAnswer;

        // Set number of possible response

        switch (difficultyChoice){
            case 1 :
                nbAnswer = 3;
                break;
            case 2 :
                nbAnswer = 5;
                break;
            default:
                nbAnswer = 2;
        }


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
        Log.e("range", "" + max);
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void setNumberOfPage(){
        TextView numPageLayout = findViewById(R.id.indexTextView);
        numPageLayout.setText(indexPage + "");
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