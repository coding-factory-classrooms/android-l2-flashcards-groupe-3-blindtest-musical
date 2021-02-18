package com.rosstail.blindtest;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataReader extends AppCompatActivity {

    private JSONObject data;

    private Map<String, String> easySongs;
    private Map<String, String> mediumSongs;
    private Map<String, String> hardSongs;
    private ArrayList<String> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //JSONObject obj = readData();
        //data = readData();
        easySongs = getEasySongs();
        mediumSongs = getMediumSongs();
        hardSongs = getHardSongs();
        answers = getAnswers();
    }

    private Map<String, String> getEasySongs() {
        return test(0);
    }

    private Map<String, String>  getMediumSongs() {
        return test(1);
    }

    private Map<String, String>  getHardSongs() {
        return test(2);
    }

    private Map<String, String> test(int difficulty) {
        Map<String, String> map = new HashMap<>();
        String difficultyStr;

        switch (difficulty) {
            default :
                difficultyStr = "easy";
                break;
            case 1 :
                difficultyStr = "medium";
                break;
            case 2 :
                difficultyStr = "hard";
                break;
        }
        JSONArray allQuestionsList;
        try {
            allQuestionsList = data.getJSONArray("questions");
            JSONObject difficultyList = allQuestionsList.getJSONObject(difficulty);
            JSONArray test = difficultyList.getJSONArray(difficultyStr);
            for (int i = 0; i < test.length(); i++) {
                JSONObject jsonObject = test.getJSONObject(i);
                map.put(jsonObject.get("artist").toString(), jsonObject.get("mp3").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    private ArrayList<String> getAnswers() {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray object = data.getJSONArray("artists");
            for (int i = 0; i < object.length(); i++) {
                list.add(object.get(i).toString());
            }
        } catch (JSONException e) {
            Log.e("BlindTestActivity", "getRandomAnswer()" + e.getCause());
        }
        return list;
    }
}
