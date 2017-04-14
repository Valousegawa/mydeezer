package com.a2017.dev.insta.mydeezer.Repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Telest on 13/04/2017.
 */

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "deezerdb";
    private static final int CURRENT_DB_VERSION = 1;

    public DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, CURRENT_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE favorite " +
            "(titre TEXT, artist TEXT, album TEXT, isFavotrite INTEGER, " +
            " sampleUrl TEXT, link TEXT, coverUrl TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1 :
                break;
            case  2 :
                break;
            case 3 :
                break;
            default:
        }
    }
}
