package com.a2017.dev.insta.mydeezer.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Telest on 11/04/2017.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        return download(urls[0]);
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        bmImage.invalidate();
    }

    private Bitmap download(String url){
        Bitmap bm = null;
        try {
            URL aUrl = new URL(url);
            URLConnection conn = aUrl.openConnection();
            conn.connect();
            InputStream in = conn.getInputStream();
            BufferedInputStream bin = new BufferedInputStream(in);
            bm = BitmapFactory.decodeStream(bin);
            bin.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }
}
