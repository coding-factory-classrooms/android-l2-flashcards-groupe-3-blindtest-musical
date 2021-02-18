package com.rosstail.blindtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SoundsListActivity extends AppCompatActivity {

    private List<SongData> songList;
    private SoundAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sounds_list);

        songList = new ArrayList<>();

        JSONObject obj = readData();
        createQuestionsList(obj);

        adapter = new SoundAdapter(songList);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void createQuestionsList (JSONObject obj){
        try {
            JSONArray allQuestionsList = obj.getJSONArray("questions");

            // Loop on different levels
            for (int i = 0; i < 3; i++){
                String keyStr = null;
                switch (i){
                    case 0 :
                        keyStr = "easy";
                        break;
                    case 1 :
                        keyStr = "medium";
                        break;
                    case 2 :
                        keyStr = "hard";
                        break;
                }
                JSONObject test = allQuestionsList.getJSONObject(i);
                //String key = test.getString("key");

                JSONArray jsonArrayByLevel = test.getJSONArray(keyStr);
                Log.d("allquestions", jsonArrayByLevel+"");

                // Loop on
                for (int j = 0; j < jsonArrayByLevel.length(); j++){

                    JSONObject item= jsonArrayByLevel.getJSONObject(j);
                    String mp3 = item.getString("mp3");
                    String artist = item.getString("artist");

                    songList.add(new SongData(mp3, artist, keyStr));

                }
            }
            Log.d("allquestions", songList + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject readData() {
        InputStream is;
        String str_data;
        try {
            is = SoundsListActivity.this.getAssets().open("data.json");
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
}