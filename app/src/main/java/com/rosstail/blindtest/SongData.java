package com.rosstail.blindtest;

import android.os.Parcel;
import android.os.Parcelable;

public class SongData implements Parcelable {
    String fileName;
    String artist;
    String difficulty;
    String title;

    public SongData(String fileName, String artist, String difficulty, String title) {
        this.fileName = fileName;
        this.artist = artist;
        this.difficulty = difficulty;
        this.title = title;
    }

    protected SongData(Parcel in) {
        fileName = in.readString();
        artist = in.readString();
        difficulty = in.readString();
        title = in.readString();
    }

    public static final Creator<SongData> CREATOR = new Creator<SongData>() {
        @Override
        public SongData createFromParcel(Parcel in) {
            return new SongData(in);
        }

        @Override
        public SongData[] newArray(int size) {
            return new SongData[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public String getArtist() {
        return artist;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(artist);
        dest.writeString(difficulty);
    }
}