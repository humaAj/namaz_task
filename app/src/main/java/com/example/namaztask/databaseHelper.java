package com.example.namaztask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "favourite.db";
    public static final String TABLE_NAME = "fav_table";
    public static final String COL_1 = "AID";
    public static final String COL_2 = "MID";
    public static final String COL_3 = "MasjidName";
    public static final String COL_4 = "fajar";
    public static final String COL_5 = "zohar";
    public static final String COL_6 = "asar";
    public static final String COL_7 = "maghrib";
    public static final String COL_8 = "isha";
    public static final String COL_9 = "jumma";
    public static final String COL_10 = "contact";
    public static final String COL_11 = "date";


    public databaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
          SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // db = this.getWritableDatabase();
        db.execSQL("create table  fav_table" + "(AID INTEGER,MID INTEGER, MasjidName TEXT, fajar TEXT, zohar TEXT,asar TEXT, maghrib TEXT, isha TEXT, jumma TEXT, contact TEXT, date TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists fav_table");
        onCreate(db);

    }

    public boolean insertData(int AID, int MID, String MasjidName,String fajar, String zohar, String asar, String maghrib, String isha, String jumma, String conatct, String date){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, AID);
        contentValues.put(COL_2, MID);
        contentValues.put(COL_3, MasjidName);
        contentValues.put(COL_4,fajar);
        contentValues.put(COL_5, zohar);
        contentValues.put(COL_6, asar);
        contentValues.put(COL_7, maghrib);
        contentValues.put(COL_8, isha);
        contentValues.put(COL_9, jumma);
        contentValues.put(COL_10, conatct);
        contentValues.put(COL_11, date);
        long result = db.insert(TABLE_NAME, null, contentValues);


        if(result == -1)
        { return false;}
        else
        {return true; }

    }
    public Cursor getAlldata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =db.rawQuery("select * from fav_table", null);
        return  res;
    }
}
