package com.a2017.dev.insta.mydeezer.model;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.a2017.dev.insta.mydeezer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Telest on 11/04/2017.
 */

public class DownloadJsonTask extends AsyncTask<String, Void, String>{

    private ArrayList<Music> musics;
    private ListView listViewMusics;
    private Context context;
    private ProgressBar p;

    public DownloadJsonTask(Context context,ArrayList <Music> musics, ListView listViewMusics){
        this.musics = musics;
        this.listViewMusics = listViewMusics;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        return download_xml(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        View parent = (View) listViewMusics.getParent();
        p = (ProgressBar) parent.findViewById(R.id.progressBar2);
        p.setVisibility(View.GONE);
        iTunesJsonToMusics(result);
    }

    @Override
    protected void onPreExecute() {
        View parent = (View) listViewMusics.getParent();
        p = (ProgressBar) parent.findViewById(R.id.progressBar2);
        p.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    private ArrayList<Music> deezerJsonToMusics(String result){
        ArrayList<Music> tmpMusics = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray tracks = jsonObject.getJSONArray("data");
            for(int i = 0; i < tracks.length(); i++){
                JSONObject track = tracks.getJSONObject(i);
                Music m = new Music();
                m.setTitre(track.getString("title"));
                m.setArtiste(track.getJSONObject("artist").getString("name"));
                m.setAlbum(track.getJSONObject("album").getString("name"));
                m.setFavorite(false);
                m.setSampleUrl(track.getString("preview"));
                m.setLink(track.getString("link"));
                m.setCoverUrl(track.getJSONObject("album").getString("cover"));
                tmpMusics.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return tmpMusics;
        }
    }

    private ArrayList<Music> iTunesJsonToMusics(String result){
        musics.clear();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray tracks = jsonObject.getJSONArray("results");
            for(int i = 0; i < tracks.length(); i++){
                JSONObject track = tracks.getJSONObject(i);
                if(track.getString("kind").equals("song")){
                    Music m = new Music();
                    m.setTitre(track.getString("trackName"));
                    m.setArtiste(track.getString("artistName"));
                    m.setAlbum(track.getString("collectionName"));
                    if(!ManageFavorites.isFavorites(context, m)){
                        m.setFavorite(false);
                    } else {
                        m.setFavorite(true);
                    }
                    m.setSampleUrl(track.getString("previewUrl"));
                    m.setLink(track.getString("trackViewUrl"));
                    m.setCoverUrl(track.getString("artworkUrl100"));
                    musics.add(m);
                    ((BaseAdapter) listViewMusics.getAdapter()).notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return musics;
        }
    }

    private String download_xml(String url){
        String result = "";
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            /*BufferedInputStream bim = new BufferedInputStream(is);
            byte[] contents = new byte[1024];
            int byteRead = 0;
            String strContents = "";
            while((byteRead = bim.read(contents)) != -1){
                strContents = new String(contents, 0, byteRead);
            }
            result = strContents;*/
            result = convertStreamToString(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String convertStreamToString(InputStream in){
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;
        try{
            while ((line = reader.readLine()) != null){
                sb.append(line).append('\n');
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                in.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
