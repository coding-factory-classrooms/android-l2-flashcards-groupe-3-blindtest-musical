package com.rosstail.blindtest;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SongList implements Parcelable {

    ArrayList<SongData> songs = new ArrayList<>();

    public SongList(ArrayList<SongData> songs) {
        this.songs = songs;
    }

    protected SongList(Parcel in) {
        songs = in.createTypedArrayList(SongData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(songs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SongList> CREATOR = new Creator<SongList>() {
        @Override
        public SongList createFromParcel(Parcel in) {
            return new SongList(in);
        }

        @Override
        public SongList[] newArray(int size) {
            return new SongList[size];
        }
    };
}
