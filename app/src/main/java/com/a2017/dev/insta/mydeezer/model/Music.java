package com.a2017.dev.insta.mydeezer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Telest on 11/04/2017.
 */

public class Music implements Serializable {
    private String titre;
    private String artiste;
    private String album;
    private boolean isFavorite;
    private String sampleUrl;
    private  String link;
    private String coverUrl;

    public Music(){
    }

    public Music(String titre, String artiste, String album, boolean isFavorite, String sampleUrl, String link, String coverUrl) {
        this.titre = titre;
        this.artiste = artiste;
        this.album = album;
        this.isFavorite = isFavorite;
        this.sampleUrl = sampleUrl;
        this.link = link;
        this.coverUrl = coverUrl;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getSampleUrl() {
        return sampleUrl;
    }

    public void setSampleUrl(String sampleUrl) {
        this.sampleUrl = sampleUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public static ArrayList<Music> getAllMusics(int n){
        ArrayList<Music> allMusics = new ArrayList<Music>(n);
        Random r = new Random();

        for(int i = 0; i < n; i++){
            int track = i+1;
            boolean io;
            int i1 = r.nextInt(80 - 65) + 65;
            if(i1%2 == 0) {
                io = true;
            } else {
                io = false;
            }
            allMusics.add(i, new Music("track - " + track, "Unknow Artist", "Unknow Album", io, "", "", ""));
        }
        return allMusics;
    }

    public static Music getDefaultMusic(){
        return new Music(
                "UN Ower Was Here ! (from Touhou)",
                "Touhou", "Touhou OST Collection",
                true,
                "https://open.spotify.com/artist/6WHjAoq6gLTudUDDdpgz3N",
                "https://open.spotify.com/artist/6WHjAoq6gLTudUDDdpgz3N",
                "https://i.ytimg.com/vi/8jJZA-O_B78/hqdefault.jpg");
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Music){
            Music tmp = (Music) obj;
            return artiste.equals(tmp.getArtiste()) && album.equals(tmp.getAlbum()) && titre.equals(tmp.getTitre());
        }
        return false;
    }
}
