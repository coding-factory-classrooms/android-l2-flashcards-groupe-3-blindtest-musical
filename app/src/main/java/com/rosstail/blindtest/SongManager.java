package com.rosstail.blindtest;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SongManager {
    SongList songList;
    AnswerList answerList;
    JSONObject data;
    SongList allSongs;

    public void loadSongs (MainActivity mainActivity){
       data = readData(mainActivity);
       cloneAnswers();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void cloneDifficultySongs(int level) {
        String difficultyStr;

        // nbOfQuestions let us set the quantity of questions for each level
        int nbOfQuestions;

        switch(level) {
            case 1 :
                difficultyStr = "medium";
                nbOfQuestions = 3;
                break;
            case 2 :
                difficultyStr = "hard";
                nbOfQuestions = 5;
                break;
            default:
                difficultyStr = "easy";
                nbOfQuestions = 2;
                break;
        }

        JSONArray allQuestionsList;
        try {
            allQuestionsList = data.getJSONArray("questions");
            JSONObject difficultyList = allQuestionsList.getJSONObject(level);
            JSONArray test = difficultyList.getJSONArray(difficultyStr);
            ArrayList<SongData> tempSongs = new ArrayList<>();
            for (int i = 0; i < nbOfQuestions; i++) {

                // this random index picks one song and put it in the questions list (songList)
                int randIndex = getRandomNumber(0, test.length());
                JSONObject jsonObject = test.getJSONObject(i);
                // remove the pick to not pick it again
                test.remove(randIndex);
                SongData song = new SongData(jsonObject.get("mp3").toString(), jsonObject.get("artist").toString(), difficultyStr);
                Log.i("" + i, jsonObject.get("artist").toString());
                tempSongs.add(song);
            }
            songList = new SongList(tempSongs);
            for (SongData aze:songList.songs) {
                Log.i("lastTest", aze.getArtist()+" / "+ aze.getFileName());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject readData(MainActivity mainActivity) {
        InputStream is;
        String str_data;
        try {
            is = mainActivity.getAssets().open("data.json");
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

    public void createQuestionsList (){
        ArrayList<SongData> tempSongs = new ArrayList<>();
        try {
            JSONArray allQuestionsList = data.getJSONArray("questions");

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
                Log.d("allquestionstest", jsonArrayByLevel+"");

                // Loop on
                for (int j = 0; j < jsonArrayByLevel.length(); j++){

                    JSONObject item= jsonArrayByLevel.getJSONObject(j);
                    String mp3 = item.getString("mp3");
                    String artist = item.getString("artist");

                    SongData newSong = new SongData(mp3, artist, keyStr);
                    Log.i("allquestionsInLoop", newSong+"");
                    //Log.i("allSongs", allSongs.songs+"");
                    tempSongs.add(newSong);
                }
            }
            allSongs = new SongList(tempSongs);
            Log.d("allquestions", songList + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getRandomNumber(int min, int max) {
        Log.e("range", "" + max);
        return (int) ((Math.random() * (max - min)) + min);
    }

}
