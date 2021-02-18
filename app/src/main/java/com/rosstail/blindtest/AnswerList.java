package com.rosstail.blindtest;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AnswerList implements Parcelable {

    ArrayList<String> answers = new ArrayList<>();

    public AnswerList(ArrayList<String> answers) {
        this.answers = answers;
    }

    protected AnswerList(Parcel in) {
        answers = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(answers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnswerList> CREATOR = new Creator<AnswerList>() {
        @Override
        public AnswerList createFromParcel(Parcel in) {
            return new AnswerList(in);
        }

        @Override
        public AnswerList[] newArray(int size) {
            return new AnswerList[size];
        }
    };
}
