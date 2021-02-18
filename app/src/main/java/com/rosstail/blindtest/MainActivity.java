package com.rosstail.blindtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });
    }
}