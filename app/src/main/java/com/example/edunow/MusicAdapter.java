package com.example.edunow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<MusicTrack> tracks;  // ✅ CHANGED: No longer MusicPlayerActivity.MusicTrack
    private OnTrackClickListener listener;

    public interface OnTrackClickListener {
        void onTrackClick(int position);
    }

    public MusicAdapter(List<MusicTrack> tracks, OnTrackClickListener listener) {
        this.tracks = tracks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_music_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicTrack track = tracks.get(position);
        holder.trackTitle.setText(track.getTitle());
        holder.trackDuration.setText(track.getDuration());

        holder.itemView.setOnClickListener(v -> listener.onTrackClick(position));
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView trackTitle;
        TextView trackDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trackTitle = itemView.findViewById(R.id.track_title);
            trackDuration = itemView.findViewById(R.id.track_duration);
        }
    }
}
