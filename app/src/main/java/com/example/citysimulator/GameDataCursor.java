package com.example.citysimulator;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.citysimulator.Schema.GameDataTable;

// GameData cursor wrapper class
public class GameDataCursor extends CursorWrapper
{
    public GameDataCursor(Cursor cursor) {
        super(cursor);
    }

    // gets the gameData and creates a new GameData based on what the values are in the database
    public GameData getGameData()
    {
        int money = getInt(getColumnIndex(GameDataTable.Cols.MONEY));
        int time = getInt(getColumnIndex(GameDataTable.Cols.TIME));
        int population = getInt(getColumnIndex(GameDataTable.Cols.POP));
        double income = getDouble(getColumnIndex(GameDataTable.Cols.INCOME));
        double employment = getDouble(getColumnIndex(GameDataTable.Cols.EMP));
        int nResidental = getInt(getColumnIndex(GameDataTable.Cols.NRES));
        int nCommercial = getInt(getColumnIndex(GameDataTable.Cols.NCOMM));
        int id = getInt(getColumnIndex(GameDataTable.Cols.ID));

        return new GameData(money, time, population, income, employment, nResidental, nCommercial, id);
    }
}
