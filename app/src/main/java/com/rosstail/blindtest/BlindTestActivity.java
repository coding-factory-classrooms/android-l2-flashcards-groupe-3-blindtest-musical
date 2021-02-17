package com.rosstail.blindtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class BlindTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind_test);

        RadioGroup group = findViewById(R.id.radioGroup);
        JSONObject obj = readData();
        if (obj == null) {
            return;
        }
        JSONObject question = getRandomTitle(obj);
        if (question == null) {
            return;
        }
        Log.i("Question", question.toString());
        addAnswers(obj, group, question, 4);
        try {
            String artist = question.get("artist").toString();

            Button confirmButton = findViewById(R.id.confirmButton);
            confirmButton.setOnClickListener(v -> {
                int checkedID = group.getCheckedRadioButtonId();
                if ( checkedID != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(checkedID);
                    if (radioButton.getText().toString().equalsIgnoreCase(artist)) {
                        Log.i("WRIGHT", "ANSWER");
                        finish();
                    } else {
                        Log.w("WRONG", "ANSWER");
                    }
                } else {
                    Log.e("Unchecked", "Not checked");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}