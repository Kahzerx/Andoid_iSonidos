package com.example.isonidos;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playSound (View vista){
        MediaPlayer m = new MediaPlayer();
        int soundId = this.getResources().getIdentifier(vista.getTag().toString(), "raw", this.getPackageName());
        m = MediaPlayer.create(this, soundId);
        m.start();
        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                if (mp != null){
                    mp.release();
                }
            }
        });
    }
}
