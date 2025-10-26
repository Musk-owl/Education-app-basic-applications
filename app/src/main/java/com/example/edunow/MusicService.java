package com.example.edunow;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.app.NotificationCompat.MediaStyle;
import android.support.v4.media.session.MediaSessionCompat;



public class MusicService extends Service {
    private static final String CHANNEL_ID = "MUSIC_CHANNEL";
    private static final int NOTIFICATION_ID = 101;
    public static final String ACTION_PLAY = "PLAY_TRACK";
    public static final String ACTION_PAUSE = "PAUSE_TRACK";
    public static final String ACTION_STOP = "STOP_TRACK";
    public static final String EXTRA_TRACK_ID = "TRACK_ID";

    private MediaPlayer mediaPlayer;
    private MediaSessionCompat mediaSession;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        mediaSession = new MediaSessionCompat(this, "EduNowMusicSession");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent != null ? intent.getAction() : null;

        if (ACTION_PLAY.equals(action)) {
            int resId = intent.getIntExtra(EXTRA_TRACK_ID, 0);
            playTrack(resId);
        } else if (ACTION_PAUSE.equals(action)) {
            pauseTrack();
        } else if (ACTION_STOP.equals(action)) {
            stopSelf();
        }

        return START_STICKY;
    }

    private void playTrack(int resId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, resId);
        if (mediaPlayer == null) {
            stopSelf();
            return;
        }

        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        startForeground(NOTIFICATION_ID, buildNotification(true));
    }

    private void pauseTrack() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            startForeground(NOTIFICATION_ID, buildNotification(false));
        } else if (mediaPlayer != null) {
            mediaPlayer.start();
            startForeground(NOTIFICATION_ID, buildNotification(true));
        }
    }

    private Notification buildNotification(boolean isPlaying) {
        PendingIntent playPauseIntent = PendingIntent.getService(
                this, 0, new Intent(this, MusicService.class)
                        .setAction(isPlaying ? ACTION_PAUSE : ACTION_PLAY),
                PendingIntent.FLAG_IMMUTABLE);

        PendingIntent stopIntent = PendingIntent.getService(
                this, 1, new Intent(this, MusicService.class)
                        .setAction(ACTION_STOP),
                PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(isPlaying ? android.R.drawable.ic_media_play : android.R.drawable.ic_media_pause)
                .setContentTitle("EduNow Focus Music")
                .setContentText(isPlaying ? "Playing relaxing sounds" : "Paused")
                .setContentIntent(stopIntent)
                .addAction(new NotificationCompat.Action(
                        isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play,
                        isPlaying ? "Pause" : "Play",
                        playPauseIntent))
                .addAction(new NotificationCompat.Action(
                        android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopIntent))
                .setStyle(new MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(isPlaying)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        return builder.build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "EduNow Focus Music",
                    NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Background focus music for study sessions");
            NotificationManager nm = getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaSession.release();
        stopForeground(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
