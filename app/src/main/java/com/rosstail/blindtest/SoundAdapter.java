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

    private List<SongData> songList;

    public SoundAdapter(List<SongData> songList) {
        this.songList = songList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rootItem :
                Context context = v.getContext();
                SongData question = (SongData) v.getTag();
                Intent intent = new Intent(context, BlindTestActivity.class);
                //intent.putExtra("question", question);
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
        SongData question = songList.get(position);

        String difficulty = question.getDifficulty();
        holder.difficulty.setText(difficulty);

        holder.itemView.setTag(question);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView difficulty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            difficulty = itemView.findViewById(R.id.difficultyTextView);
        }
    }
}