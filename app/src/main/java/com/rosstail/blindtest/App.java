package com.rosstail.blindtest;

import android.app.Application;

public class App extends Application {
    SongManager songManager;


    @Override
    public void onCreate() {
        super.onCreate();

        songManager = new SongManager();
    }
}
