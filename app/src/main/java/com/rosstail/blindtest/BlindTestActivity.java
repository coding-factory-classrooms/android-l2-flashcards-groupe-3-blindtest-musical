package com.rosstail.blindtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BlindTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind_test);

        RadioGroup group = findViewById(R.id.radioGroup);
        RadioButton button;
        JSONObject obj = readData();
        if (obj == null) {
            return;
        }
        Object question = getRandomTitle(obj);
        if (question == null) {
            return;
        }
        Log.i("Question", question.toString());
        for(int i = 0; i < 3; i++) {
            button = new RadioButton(this);
            button.setText("Button " + i);
            group.addView(button);
        }
    }

    private Object getRandomTitle(JSONObject data) {
        try {
            JSONArray titleList = data.getJSONArray("questions");
            int i = getRandomNumber(0, titleList.length());
            return titleList.get(i);
        } catch (JSONException e) {
            Log.e("BlindeTestActivity", "getRandomTitle()" + e.getCause());
        }
        return null;
    }

    private JSONObject readData() {
        InputStream is;
        String str_data = "";
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