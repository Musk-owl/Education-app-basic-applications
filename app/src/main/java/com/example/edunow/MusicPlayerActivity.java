package com.example.edunow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {
    private ListView listTracks;
    private Button stopMusic;
    private final int[] rawTracks = {
            R.raw.calm_piano,
            R.raw.nature_sounds,
            R.raw.lofi_study,
            R.raw.white_noise
    };
    private final String[] trackNames = { "Calm Piano", "Nature Sounds", "Lo-Fi Study", "White Noise" };
    private final String[] trackDurations = { "5:00", "6:30", "4:10", "10:00" }; // Example durations

    private List<MusicTrack> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        listTracks = findViewById(R.id.list_tracks);
        stopMusic = findViewById(R.id.button_stop);

        tracks = new ArrayList<>();
        // Populate the list only once
        for (int i = 0; i < trackNames.length; i++) {
            tracks.add(new MusicTrack(trackNames[i], rawTracks[i], trackDurations[i]));
        }

        // Use adapter with fixed data list
        MusicListAdapter adapter = new MusicListAdapter(this, tracks);
        listTracks.setAdapter(adapter);

        listTracks.setOnItemClickListener((parent, view, position, id) -> {
            playTrackInService(tracks.get(position).getResourceId());
            Toast.makeText(this, "Playing: " + tracks.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        });

        stopMusic.setOnClickListener(v -> stopMusicService());
    }

    private void playTrackInService(int resId) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.ACTION_PLAY);
        intent.putExtra(MusicService.EXTRA_TRACK_ID, resId);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void stopMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.ACTION_STOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    // Custom Adapter for MusicTrack objects
    private static class MusicListAdapter extends ArrayAdapter<MusicTrack> {
        public MusicListAdapter(android.content.Context context, List<MusicTrack> tracks) {
            super(context, R.layout.item_music_track, tracks);
        }

        @Override
        public View getView(int position, View convertView, android.view.ViewGroup parent) {
            if (convertView == null) {
                convertView = android.view.LayoutInflater.from(getContext())
                        .inflate(R.layout.item_music_track, parent, false);
            }
            MusicTrack track = getItem(position);
            TextView title = convertView.findViewById(R.id.track_title);
            TextView duration = convertView.findViewById(R.id.track_duration);

            if (track != null) {
                title.setText(track.getTitle());
                duration.setText(track.getDuration());
            }
            return convertView;
        }
    }
}
