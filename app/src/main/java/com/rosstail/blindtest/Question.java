package com.rosstail.blindtest;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    String fileName;
    String artist;
    String difficulty;

    public Question(String fileName, String artist, String difficulty) {
        this.fileName = fileName;
        this.artist = artist;
        this.difficulty = difficulty;
    }

    protected Question(Parcel in) {
        fileName = in.readString();
        artist = in.readString();
        difficulty = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
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
