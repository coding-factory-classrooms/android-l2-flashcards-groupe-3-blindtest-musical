package com.rosstail.blindtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    JSONObject data;
    SongList songList;
    AnswerList answerList;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = readData();
        cloneAnswers();

        Button aboutButton = findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //playClick();
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        Button blindTestButton = findViewById(R.id.blindTestButton);
        blindTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] difficulties = {"Facile", "Moyen", "Difficile"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choisir une difficulté");
                builder.setItems(difficulties, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, BlindTestActivity.class);
                        Log.e("mainActivity", difficulties[which]);
                        intent.putExtra("difficulty", which);
                        cloneDifficultySongs(which);
                        intent.putExtra("songs", (Parcelable) songList);
                        intent.putExtra("answers", (Parcelable) answerList);
                        intent.putExtra("titleNumber", getTitleNumber(which));
                        intent.putExtra("answerNumber", getAnswerNumber(which));
                        intent.putExtra("score", 0);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });
    }




    private JSONObject readData() {
        InputStream is;
        String str_data;
        try {
            is = MainActivity.this.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size]; //declare the size of the byte array with size of the file
            is.read(buffer);
            is.close();
            str_data = new String(buffer);
            Log.i("test", new JSONObject(str_data).toString());
            return new JSONObject(str_data);
            //Log.w("test", new JSONObject(str_data).getJSONArray("artistes").toString());
        } catch (IOException | JSONException e) {
            Log.e("BlindTestActivity", "Read Data Error");
            e.printStackTrace();
        }
        //Log.w("BlindTest onCreate", str_data);
        return null;
    }


    /**
     * Clone
     * @param level
     * @return
     */
    private void cloneDifficultySongs(int level) {
        String difficultyStr;
        switch(level) {
            case 1 :
                difficultyStr = "medium";
                break;
            case 2 :
                difficultyStr = "hard";
                break;
            default:
                difficultyStr = "easy";
                break;
        }

        JSONArray allQuestionsList;
        try {
            allQuestionsList = data.getJSONArray("questions");
            JSONObject difficultyList = allQuestionsList.getJSONObject(level);
            JSONArray test = difficultyList.getJSONArray(difficultyStr);
            ArrayList<SongData> tempSongs = new ArrayList<>();
            for (int i = 0; i < test.length(); i++) {
                JSONObject jsonObject = test.getJSONObject(i);
                SongData song = new SongData(jsonObject.get("mp3").toString(), jsonObject.get("artist").toString(), difficultyStr);
                tempSongs.add(song);
            }
            songList = new SongList(tempSongs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cloneAnswers() {
        try {
            JSONArray allQuestionsList = data.getJSONArray("artists");
            ArrayList<String> answers = new ArrayList<>();
            for (int i = 0; i < allQuestionsList.length(); i++) {
                answers.add(allQuestionsList.get(i).toString());
            }
            answerList = new AnswerList(answers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getTitleNumber(int level) {
        switch(level) {
            default:
                return 2;
            case 1 :
                return 3;
            case 2 :
                return 4;
        }
    }

    private int getAnswerNumber(int level) {
        switch(level) {
            default:
                return 4;
            case 1 :
                return 6;
            case 2 :
                return 8;
        }
    }
}