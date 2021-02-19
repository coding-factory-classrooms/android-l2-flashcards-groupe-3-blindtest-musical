package com.rosstail.blindtest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.ViewHolder> implements View.OnClickListener {

    private final SongList songList;
    AnswerList allSongs;

    public SoundAdapter(SongList songList, AnswerList allSongs) {
        this.songList = songList;
        this.allSongs = allSongs;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rootItem :
                Context context = v.getContext();
                SongData question = (SongData) v.getTag();
                Intent intent = new Intent(context, BlindTestActivity.class);
                intent.putExtra("question", question);
                intent.putExtra("answers", allSongs);
                context.startActivity(intent);
                break;
        }
    }

    @NonNull
    @Override
    public SoundAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sound, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundAdapter.ViewHolder holder, int position) {
        SongData question = songList.songs.get(position);

        String difficulty = question.getDifficulty();
        holder.difficulty.setText(difficulty + "\n" + question.getTitle());

        holder.itemView.setTag(question);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return songList.songs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView difficulty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            difficulty = itemView.findViewById(R.id.difficultyTextView);
        }
    }
}
