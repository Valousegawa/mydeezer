package com.a2017.dev.insta.mydeezer.Repository;

import com.a2017.dev.insta.mydeezer.model.Music;

import java.util.ArrayList;

/**
 * Created by Telest on 13/04/2017.
 */

public interface IFavoriteRepository {
    boolean add(Music m);

    boolean remove(Music m);

    boolean isFavorite(Music m);

    ArrayList<Music> getAll();
}
