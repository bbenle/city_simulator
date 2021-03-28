package com.example.citysimulator;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.citysimulator.Schema.SettingsTable;

// Settings cursor wrapper class
public class SettingsCursor extends CursorWrapper
{
    public SettingsCursor(Cursor cursor) {
        super(cursor);
    }

    // gets the settings and creates a new settings based on what the values are in the database
    public Settings getSettings()
    {
        int height = getInt(getColumnIndex(SettingsTable.Cols.HEIGHT));
        int width = getInt(getColumnIndex(SettingsTable.Cols.WIDTH));
        int initialMoney = getInt(getColumnIndex(SettingsTable.Cols.INITMONEY));
        int id = getInt(getColumnIndex(SettingsTable.Cols.ID));

        return new Settings(height, width, initialMoney, id);
    }
}
