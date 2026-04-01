package com.example.luckydroids;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "LuckyDroids.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "my_slots";
    private static final String COLUMN_ID = "_id";
    //private static final String COLUMN_GAMES = "games_history";
    private static final String COLUMN_PLAYER = "games_player";
    private static final String COLUMN_MONEY = "games_money";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE" + TABLE_NAME +
                        " (" + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        //COLUMN_HISTORY + " TEXT, " +
                        COLUMN_PLAYER + " TEXT, " +
                        COLUMN_MONEY + " INTEGER);";
        db.execSQL(query);
        //CREATE TABLE my_slots (_id iNTEGER PRIMARY KEY AUTOINCREMENT, games_history TEXT, games_player TEXT, games_money INTEGER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
