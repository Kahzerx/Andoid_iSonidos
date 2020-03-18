package com.example.isonidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.lang.reflect.Field;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap <String, String> soundList = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout principal = (LinearLayout) findViewById(R.id.buttons);
        int lineNumber = 0;
        LinearLayout aux = createButtonLine(lineNumber);
        principal.addView(aux);

        Field[] songList = R.raw.class.getFields();
        int columns = 5;
        for (int i = 0; i < songList.length; i++){
            Button b = createButton(i, songList);
            aux.addView(b);
            if (i % columns == columns - 1){
                aux = createButtonLine(i);
                principal.addView(aux);
            }
            soundList.put(b.getTag().toString(), b.getText().toString());
            b.setText(shortButtonName(b.getText().toString()).replace("_", " "));
        }
    }

    private LinearLayout createButtonLine (int lineNumber){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        LinearLayout line = new LinearLayout(this);

        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setLayoutParams(params);
        line.setId(lineNumber);

        return line;
    }

    private Button createButton (int i, Field[] _songList){
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;
        buttonParams.setMargins(5, 5, 5, 5);
        buttonParams.gravity = Gravity.CENTER_HORIZONTAL;
        Button b = new Button(this);
        b.setLayoutParams(buttonParams);
        b.setText(_songList[i].getName());
        b.setTextColor(Color.WHITE);
        b.setTextSize(10);
        b.setBackgroundColor(Color.BLUE);
        b.setAllCaps(true);
        int id = this.getResources().getIdentifier(_songList[i].getName(), "raw", getPackageName());
        String longName = _songList[i].getName();

        if (longName != null && longName.substring(0,2).contains("v_")){
            b.setBackgroundColor(Color.rgb(255, 140, 0));
        }
        b.setTag(id);
        b.setId(i + 50);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                sound(view);
            }
        });

        return b;
    }

    public void sound(View view){
        Button b = (Button) findViewById(view.getId());
        String name = soundList.get(view.getTag().toString());
        if (name.substring(0,2).contains("v_")){
            VideoView videoView = (VideoView) findViewById(R.id.videoView);
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + view.getTag());
            videoView.setVideoURI(uri);
            videoView.start();
        }
        else{
            MediaPlayer mp = new MediaPlayer();
            mp = MediaPlayer.create(this, (int) findViewById(view.getId()).getTag());
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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

    private String shortButtonName (String s){
        if (s.substring(0,2).contains("v_")){
            s = s.substring((s.indexOf('_') + 1));
        }
        if (s.contains("_")){
            s = s.substring(s.indexOf('_'));
        }
        s.replace('_', ' ');

        return s;
    }
}
