package com.rosstail.blindtest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
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

    SongManager songManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App app = (App) getApplication();

        songManager = app.songManager;
        songManager.loadSongs(this);

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
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, BlindTestActivity.class);
                        Log.e("mainActivity", difficulties[which]);
                        intent.putExtra("difficulty", which);
                        songManager.cloneDifficultySongs(which);
                        intent.putExtra("songs", (Parcelable) songManager.songList);
                        intent.putExtra("answers", (Parcelable) songManager.answerList);
                        intent.putExtra("titleNumber", getTitleNumber(which));
                        //intent.putExtra("answerNumber", getAnswerNumber(which));
                        //intent.putExtra("score", 0);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });

        Button allQuestionsButton = findViewById(R.id.allQuestionsButton);
        allQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SoundsListActivity.class);
                startActivity(intent);
            }
        });
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