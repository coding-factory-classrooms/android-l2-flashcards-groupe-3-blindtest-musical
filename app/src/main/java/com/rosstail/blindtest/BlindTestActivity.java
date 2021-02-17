package com.rosstail.blindtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
            return;
        }
        JSONObject question = getRandomTitle(obj);
        if (question == null) {
            return;
        }

        // Create Media Player //

        String fileName = null;
        try {
            fileName = question.get("mp3").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("blindtestactivity", fileName);
        int fileId = this.getResources().getIdentifier("mj_thriller.mp3", "raw", this.getPackageName());

        createPlayer("mj_thriller");

        // ------------------- //

        Log.i("Question", question.toString());
        addAnswers(obj, group, question, 4);
    }

    private JSONObject getRandomTitle(JSONObject data) {
        try {
            JSONArray titleList = data.getJSONArray("questions");
            int i = getRandomNumber(0, titleList.length());
            return titleList.getJSONObject(i);
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
        Log.w("init list", list.toString());
        list.remove(artist);
        for(int i = 0; i < nbAnswer; i++) {
            Log.w("list step " + i, list.toString());
            String answer = list.get(getRandomNumber(0, list.size()));
            RadioButton button = new RadioButton(this);
            button.setText(answer);
            group.addView(button);
            list.remove(answer);
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

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            //case R.id.playerButton :
              //  createPlayer("mj_thriller");
                //break;
            case R.id.stopButton :
                mp.stop();
        }
    }*/
}