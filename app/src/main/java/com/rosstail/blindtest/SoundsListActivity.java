package com.rosstail.blindtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class SoundsListActivity extends AppCompatActivity {

    private SoundAdapter adapter;
    private SongManager songManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sounds_list);

        App app = (App) getApplication();
        songManager = app.songManager;

        songManager.createQuestionsList();

        adapter = new SoundAdapter(songManager.allSongs, songManager.answerList);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}