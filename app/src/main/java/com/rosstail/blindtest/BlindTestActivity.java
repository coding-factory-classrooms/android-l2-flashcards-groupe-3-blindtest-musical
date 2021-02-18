package com.rosstail.blindtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BlindTestActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind_test);

        findViewById(R.id.playerButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);

        RadioGroup group = findViewById(R.id.radioGroup);
        JSONObject obj = readData();
        if (obj == null) {
            finish();
            return;
        }
        // get difficulty choice of the user

        Intent srcIntent = getIntent();
        int difficultyChoice = srcIntent.getIntExtra("difficulty", 1);

        Log.e("blindactivitytest", difficultyChoice + "");

        JSONObject question = getRandomTitle(obj, difficultyChoice);

        Log.e("blindactivitytest", question + "");

        if (question == null) {
            finish();
            return;
        }

        // Create Media Player //

        String fileName = null;
        try {
            fileName = question.get("mp3").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("player activity", fileName);
        String playerFileName = fileName.replace(".mp3", "");
        Log.d("test123", playerFileName);
        createPlayer(playerFileName);

        // ------------------- //

        Log.i("Question", question.toString());
        addAnswers(obj, group, question, 4);
        try {
            String artist = question.get("artist").toString();
            setConfirm(group, artist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setConfirm(RadioGroup group, String artist) {
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            int checkedID = group.getCheckedRadioButtonId();
            if (checkedID != -1) {
                RadioButton radioButton = (RadioButton) findViewById(checkedID);
                TextView label = findViewById(R.id.responseTextView);
                if (radioButton.getText().toString().equalsIgnoreCase(artist)) {
                    label.setText("Right answer !");
                } else {
                    label.setText("Too bad ! The correct answer is " + artist);
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

    private JSONObject getRandomTitle(JSONObject data, int difficulty) {
        String difficultyStr = "easy";
        switch (difficulty){
            case 0 :
                difficultyStr = "easy";
                break;
            case 1 :
                difficultyStr = "medium";
                break;
            case 2 :
                difficultyStr = "hard";
                break;
        }
        try {
            JSONArray allQuestionsList = data.getJSONArray("questions");

            JSONObject difficultyList = allQuestionsList.getJSONObject(difficulty);
            JSONArray test = difficultyList.getJSONArray(difficultyStr);

            Log.e("blindactivitytest", difficultyList.toString());

            int i = getRandomNumber(0, test.length());
            return test.getJSONObject(i);
        } catch (JSONException e) {
            Log.e("BlindTestActivity", "getRandomTitle()" + e.getCause());
        }
        return null;
    }

    private void addAnswers(JSONObject obj, RadioGroup group, JSONObject question, int nbAnswer) {
        JSONArray answersJson = getAnswers(obj);
        if (answersJson == null) {
            return;
        }

        ArrayList<String> list = new ArrayList<>();
        int len = answersJson.length();
        for (int i=0;i<len;i++){
            try {
                list.add(answersJson.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String artist = null;
        try {
            artist = question.get("artist").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (artist == null) {
            return;
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

    private JSONArray getAnswers(JSONObject data) {
        try {
            return data.getJSONArray("artists");
        } catch (JSONException e) {
            Log.e("BlindTestActivity", "getRandomAnswer()" + e.getCause());
        }
        return null;
    }

    private JSONObject readData() {
        InputStream is;
        String str_data;
        try {
            is = BlindTestActivity.this.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size]; //declare the size of the byte array with size of the file
            is.read(buffer);
            is.close();
            str_data = new String(buffer);
            return new JSONObject(str_data);
            //Log.w("test", new JSONObject(str_data).getJSONArray("artistes").toString());
        } catch (IOException | JSONException e) {
            Log.e("BlindTestActivity", "Read Data Error");
            e.printStackTrace();
        }
        //Log.w("BlindTest onCreate", str_data);
        return null;
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