package com.a2017.dev.insta.mydeezer.model;

import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.a2017.dev.insta.mydeezer.R;

import java.util.ArrayList;

/**
 * Created by Telest on 11/04/2017.
 */

public class MusicAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<Music> musics;

    public MusicAdapter(Activity context, ArrayList<Music> musics) {
        this.context = context;
        this.musics = musics;
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int position) {
        return musics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.details_layout, null);
        ImageView imageviewAlbum = (ImageView) rowView.findViewById(R.id.imgViewCoverAlbum);
        TextView textViewMusic = (TextView) rowView.findViewById(R.id.txtViewTitre);
        ImageView imageViewFavoris = (ImageView) rowView.findViewById(R.id.imgViewFavourite);

        textViewMusic.setText(musics.get(position).getArtiste() + "\n" + musics.get(position).getTitre());
        if(musics.get(position).isFavorite()){
            imageViewFavoris.setImageResource(drawable.star_on);
        } else {
            imageViewFavoris.setImageResource(drawable.star_off);
        }
        DownloadImageTask imageTask = new DownloadImageTask(imageviewAlbum);
        imageTask.execute(musics.get(position).getCoverUrl());

        return rowView;
    }
}