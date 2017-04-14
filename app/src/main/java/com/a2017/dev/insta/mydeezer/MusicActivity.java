package com.a2017.dev.insta.mydeezer;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.a2017.dev.insta.mydeezer.model.DownloadImageTask;
import com.a2017.dev.insta.mydeezer.model.ManageFavorites;
import com.a2017.dev.insta.mydeezer.model.Music;
import com.victor.loading.rotate.RotateLoading;


/**
 * Created by Telest on 11/04/2017.
 */

public class MusicActivity extends Activity {

    TextView fieldTitle, fieldArtist, fieldAlbum, lecture;
    Switch favorite;
    ImageView cover;

    Button play, link, stop;
    RotateLoading spinner;

    private Music currentMusic;
    final MediaPlayer player = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_layout);

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        fieldTitle = (TextView) findViewById(R.id.textViewTitre);
        fieldAlbum = (TextView) findViewById(R.id.textViewAlbum);
        fieldArtist = (TextView) findViewById(R.id.textViewArtiste);
        lecture = (TextView) findViewById(R.id.textViewPlaying);
        lecture.setVisibility(View.GONE);

        favorite = (Switch) findViewById(R.id.switchFavoris);
        cover = (ImageView) findViewById(R.id.imgViewCoverAlbum);

        play = (Button) findViewById(R.id.buttonPlay);
        link = (Button) findViewById(R.id.buttonLink);
        stop = (Button) findViewById(R.id.buttonStop);

        spinner = (RotateLoading) findViewById(R.id.progressBar);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    player.reset();
                    player.setDataSource(currentMusic.getSampleUrl());
                    player.prepare();
                    player.start();

                    stop.setVisibility(View.VISIBLE);
                    spinner.start();
                    lecture.setVisibility(View.VISIBLE);

                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            spinner.setVisibility(View.INVISIBLE);
                            lecture.setVisibility(View.INVISIBLE);
                            stop.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();

                stop.setVisibility(View.INVISIBLE);
                spinner.stop();
                lecture.setVisibility(View.INVISIBLE);
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(currentMusic.getLink()));
                startActivity(viewIntent);
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(buttonView.isChecked()){
                            currentMusic.setFavorite(true);
                            ManageFavorites.add(getApplicationContext(), currentMusic);
                        } else {
                            currentMusic.setFavorite(false);
                            ManageFavorites.remove(getApplicationContext(), currentMusic);
                        }
                    }
                });
            }

        });
    }

    public void setSelectedMusic(Music m){
        currentMusic = m;
        fieldTitle.setText(currentMusic.getTitre());
        fieldArtist.setText(currentMusic.getArtiste());
        fieldAlbum.setText(currentMusic.getAlbum());

        favorite.setChecked(currentMusic.isFavorite());
        DownloadImageTask imageTask = new DownloadImageTask(cover);
        imageTask.execute(currentMusic.getCoverUrl());

        favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentMusic.setFavorite(isChecked);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent myIntent = getIntent();
        Music music = (Music) myIntent.getSerializableExtra("musicSelected");
        setSelectedMusic(music);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            player.reset();
        }
        return super.onKeyDown(keyCode, event);
    }
}
