package com.a2017.dev.insta.mydeezer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.a2017.dev.insta.mydeezer.Repository.FavoriteRepository;
import com.a2017.dev.insta.mydeezer.model.DownloadJsonTask;
import com.a2017.dev.insta.mydeezer.model.IOnFavoriteChange;
import com.a2017.dev.insta.mydeezer.model.ManageFavorites;
import com.a2017.dev.insta.mydeezer.model.Music;
import com.a2017.dev.insta.mydeezer.model.MusicAdapter;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private EditText searchText;
    private ImageButton searchButton;
    private ListView listViewMusics;

    private ArrayList<Music> musics;
    private MusicAdapter adapter;

    private static final String URL_MUSIC_SEARCH = "https://itunes.apple.com/search?term=";
    private static final int ACTION_SELECT = 1;
    private static final int ACTION_FAV_ON = 2;
    private static final int ACTION_FAV_OFF = 3;

    FavoriteRepository fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Toast.makeText(getApplicationContext(), String.valueOf(isChecked),Toast.LENGTH_SHORT).show();
        ArrayList<Music> mm = new ArrayList<>();
        musics = mm;
        fr = new FavoriteRepository(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.empty_layout);
        ManageFavorites.setListenen(new IOnFavoriteChange() {
            @Override
            public void onFavoriteChange(Music m, boolean isFavourite) {
                musics.get(musics.indexOf(m)).setFavorite(isFavourite);
                ((BaseAdapter) listViewMusics.getAdapter()).notifyDataSetChanged();
            }
        });

        searchText = (EditText) findViewById(R.id.editTextSearch);
        searchButton = (ImageButton) findViewById(R.id.imgBtnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchText.getText().toString().equals("")){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    searchMusic(searchText.getText().toString());
                }
            }
        });

        listViewMusics = (ListView) findViewById(R.id.listViewResults);
        adapter = new MusicAdapter(this, musics);
        listViewMusics.setAdapter(adapter);
        listViewMusics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToMusic(position);
            }
        });
        listViewMusics.setEmptyView(findViewById(R.id.splashSearch));

        listViewMusics.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(Menu.NONE, ACTION_SELECT, 0, "Sélectionner");
                AdapterView.AdapterContextMenuInfo adapterContext = (AdapterView.AdapterContextMenuInfo) menuInfo;
                menu.setHeaderTitle(musics.get(adapterContext.position).getTitre());
                if(musics.get(adapterContext.position).isFavorite()){
                    menu.add(Menu.NONE, ACTION_FAV_OFF, 1, "Retirer");
                } else {
                    menu.add(Menu.NONE, ACTION_FAV_ON, 1, "Ajouter");
                }
            }
        });
    }

    public void goToMusic(int position){
        Intent musicIntent = new Intent(getApplicationContext(), MusicActivity.class);
        musicIntent.putExtra("musicSelected", musics.get(position));
        startActivity(musicIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new MusicAdapter(this, musics);
        listViewMusics.setAdapter(adapter);
        listViewMusics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToMusic(position);
            }
        });
    }

    public void searchMusic(String textSearch){
        String url = URL_MUSIC_SEARCH + textSearch.replace(" ", "+");
        DownloadJsonTask jSonTask = new DownloadJsonTask(this, musics, listViewMusics);
        jSonTask.execute(url);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case ACTION_SELECT :
                goToMusic(info.position);
                break;
            case ACTION_FAV_ON :
                musics.get(info.position).setFavorite(true);
                ManageFavorites.add(this, musics.get(info.position));
                break;
            case ACTION_FAV_OFF :
                musics.get(info.position).setFavorite(false);
                ManageFavorites.remove(this, musics.get(info.position));
                break;
        }
        ((BaseAdapter) listViewMusics.getAdapter()).notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_favorites){
            musics = ManageFavorites.load(this);
            adapter = new MusicAdapter(this, musics);
            listViewMusics.setAdapter(adapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
