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

    SongList songList;
    ArrayList<SongData> songDatas;
    SongData rightAnswer;
    AnswerList answerList;
    int difficultyChoice;
    int numberOfQuestions;
    int score;
    int indexPage = 1;
    SongData songFromList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind_test);

        findViewById(R.id.playerButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);

        RadioGroup group = findViewById(R.id.radioGroup);

        Intent srcIntent = getIntent();
        answerList = (AnswerList) srcIntent.getParcelableExtra("answers");

        songFromList = srcIntent.getParcelableExtra("question");
        if (songFromList != null){
            difficultyChoice = getIndexOfDifficulty(songFromList.difficulty);
            songDatas = new ArrayList<>();
            songDatas.add(songFromList);
            Log.e("lalala", songFromList.getArtist());
            displaySong(songFromList, group);
        }
        else {
            difficultyChoice = srcIntent.getIntExtra("difficulty", 1);
            songList = srcIntent.getParcelableExtra("songs");
            songDatas = (ArrayList<SongData>) songList.songs.clone();
            int index = getRandomNumber(0, songDatas.size());
            rightAnswer = songDatas.get(index);
            displaySong(rightAnswer, group);
        }
        // Set the number of pages
        TextView offPage = findViewById(R.id.offPageTextView);
        offPage.setText("/ " + songDatas.size());
        //numberAnswers = songs.size();
        numberOfQuestions = songDatas.size();



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

            // No more question, we go to ScoreActivity
            if (songDatas.size() <= 0 || indexPage == numberOfQuestions) {
                Log.e("Game Over", score + " / " + numberOfQuestions);

                // case we are here from the list
                if (songFromList != null){
                    finish();
                    mp.stop();
                    return;
                }

                Intent intent = new Intent(BlindTestActivity.this, ScoreActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("numberOfQuestions", numberOfQuestions);

                String levelScore;
                switch (difficultyChoice){
                    case 1 :
                        levelScore = "Moyen";
                        break;
                    case 2 :
                        levelScore = "Difficile";
                        break;
                    default:
                        levelScore = "Facile";
                }

                intent.putExtra("level", levelScore);
                startActivity(intent);
                mp.stop();
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

        Log.i("repetitionreponse", difficultyChoice +"");

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

    public int getIndexOfDifficulty(String levelStr){
        int returnInt;
        switch (levelStr){
            case "medium" :
                returnInt = 1;
                break;
            case "hard" :
                returnInt = 2;
                break;
            default:
                returnInt = 0;
        }
        return returnInt;
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