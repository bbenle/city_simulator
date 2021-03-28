package com.example.citysimulator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.citysimulator.Schema.SettingsTable;
import com.example.citysimulator.Schema.GameDataTable;
import com.example.citysimulator.Schema.GameElementTable;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final int VERSION = 1; // current version of the database
    public static final String DB_NAME = "citysimulator.db"; // name of the database

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // settings database
        // stores the name, id, height, width and initial money
        db.execSQL("CREATE TABLE " + SettingsTable.NAME + "(" +
                SettingsTable.Cols.ID + " INTEGER, " +
                SettingsTable.Cols.HEIGHT + " INTEGER, " +
                SettingsTable.Cols.WIDTH + " INTEGER, " +
                SettingsTable.Cols.INITMONEY + " INTEGER)");

        // gameData database
        // stores the name, id, money, time, population, income, employment, residentialNum and commericallNum
        db.execSQL("CREATE TABLE " + GameDataTable.NAME + "(" +
                GameDataTable.Cols.ID + " INTEGER, " +
                GameDataTable.Cols.MONEY + " INTEGER, " +
                GameDataTable.Cols.TIME + " INTEGER, " +
                GameDataTable.Cols.POP + " INTEGER, " +
                GameDataTable.Cols.INCOME + " REAL, " +
                GameDataTable.Cols.EMP + " REAL, " +
                GameDataTable.Cols.NRES + " INTEGER, " +
                GameDataTable.Cols.NCOMM + " INTEGER)");

        // gameElement database
        // stores the ownername, structure, row and column
        db.execSQL("CREATE TABLE " + GameElementTable.NAME + "(" +
                GameElementTable.Cols.OWNERNAME + " TEXT, " +
                GameElementTable.Cols.STRUCTURE + " BLOB, " +
                GameElementTable.Cols.R + " INTEGER, " +
                GameElementTable.Cols.C + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        // no code needed here
    }
}
