package com.example.car_crash;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class EffectsManager {


    private static MediaPlayer mediaPlayer;

    public static void playFailureSound(Context context) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(context, R.raw.failure_sound);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mediaPlayer = null;
            }
        });

        mediaPlayer.start();
    }


    public static void playGameOverSound(Context context) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(context, R.raw.gameover);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mediaPlayer = null;
            }
        });

        mediaPlayer.start();
    }


    public static void vibrate(Object systemService) {
        ((Vibrator) systemService).vibrate(200);
    }
}
