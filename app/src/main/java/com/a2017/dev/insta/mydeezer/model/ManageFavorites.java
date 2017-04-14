package com.a2017.dev.insta.mydeezer.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Telest on 12/04/2017.
 */

public class ManageFavorites {

    private static IOnFavoriteChange listenen;

    public static void setListenen(IOnFavoriteChange listenen) {
        ManageFavorites.listenen = listenen;
    }

    public static void save(Context context, ArrayList<Music> musics){
        SharedPreferences appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(musics);
        prefsEditor.putString("musicFavorites", json);
        prefsEditor.commit();
    }

    public static ArrayList<Music> load(Context context){
        SharedPreferences appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = appSharedPreferences.getString("musicFavorites", "");
        List<Music> musics;
        if(json.equals("")){
            musics = new ArrayList<>();
        } else {
            Music[] favoritesMusics = gson.fromJson(json, Music[].class);
            musics = Arrays.asList(favoritesMusics);
            musics = new ArrayList<Music>(musics);
        }
        return (ArrayList<Music>) musics;
    }

    public static void add(Context context, Music music){
        ArrayList<Music> favorites = load(context);
        if(!favorites.contains(music)){
            favorites.add(music);
            listenen.onFavoriteChange(music, true);
        }
        save(context, favorites);
    }

    public  static void remove(Context context, Music music){
        ArrayList<Music> favorites = load(context);
        favorites.remove(music);
        listenen.onFavoriteChange(music, false);
        save(context, favorites);
    }

    public static boolean isFavorites(Context context, Music music){
        ArrayList<Music> favorites = load(context);
        return favorites.contains(music);
    }

    public static void getFavs(Context context){
        ArrayList<Music> favorites = load(context);
        Log.e("favs", favorites.toString());
    }
}
