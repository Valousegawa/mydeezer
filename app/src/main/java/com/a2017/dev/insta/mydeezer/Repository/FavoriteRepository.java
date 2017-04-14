package com.a2017.dev.insta.mydeezer.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.a2017.dev.insta.mydeezer.model.Music;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Telest on 13/04/2017.
 */

public class FavoriteRepository implements IFavoriteRepository {

    private DataBaseManager dbm;

    public FavoriteRepository(Context context) {
        dbm = new DataBaseManager(context);
    }

    @Override
    public boolean add(Music m) {
        if(isFavorite(m)){
            return false;
        }
        ContentValues v = new ContentValues();
        v.put("title", m.getTitre());
        v.put("artist", m.getArtiste());
        v.put("album", m.getAlbum());
        v.put("isFavorite", m.isFavorite());
        v.put("sampleUrl", m.getSampleUrl());
        v.put("link", m.getLink());
        v.put("coverUrl", m.getCoverUrl());
        long line = dbm.getWritableDatabase().insert("favorite", null, v);
        return line != 0;
    }

    @Override
    public boolean remove(Music m) {
        String[] identifier = {m.getTitre(), m.getArtiste(), m.getAlbum()};
        long line = dbm.getWritableDatabase().delete("favorite", "titre=? and album=? and artist=?", identifier);
        return line != 0;
    }

    @Override
    public boolean isFavorite(Music m) {
        String[] identifier = {m.getTitre(), m.getArtiste(), m.getAlbum()};
        Cursor c = dbm.getWritableDatabase().rawQuery("SELECT * FROM favorite WHERE titre=? and album=? and artist=?", identifier);
        return c.getCount() > 0;
    }

    @Override
    public ArrayList<Music> getAll() {
        ArrayList<Music> m = new ArrayList<>();
        Cursor c = dbm.getWritableDatabase().rawQuery("SELECT* FROM favorite", null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            Music mm = new Music();
            mm.setTitre(c.getString(0));
            mm.setArtiste(c.getString(1));
            mm.setAlbum(c.getString(2));
            mm.setFavorite(c.getInt(3) == 1);
            mm.setSampleUrl(c.getString(4));
            mm.setLink(c.getString(5));
            mm.setCoverUrl(c.getString(6));

            m.add(mm);
        }
        c.close();
        return m;
    }
}
