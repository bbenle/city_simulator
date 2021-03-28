package com.example.citysimulator;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.citysimulator.Schema.GameElementTable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

// GameElement cursor wrapper class
public class GameElementCursor extends CursorWrapper
{
    public GameElementCursor(Cursor cursor) {
        super(cursor);
    }

    // gets the gameElement and creates a new gameElement based on what the values are in the database
    public GameElement getGameElement()
    {
        String name = getString(getColumnIndex(GameElementTable.Cols.OWNERNAME));
        Structure structure = getStructure(getBlob(getColumnIndex(GameElementTable.Cols.STRUCTURE)));
        int row = getInt(getColumnIndex(GameElementTable.Cols.R));
        int col = getInt(getColumnIndex(GameElementTable.Cols.C));

        return new GameElement(structure, name, row, col);
    }

    // method to convert byte array to object again
    // this code was obtained from this source https://stackoverflow.com/questions/1243181/how-to-store-object-in-sqlite-database
    public Structure getStructure(byte[] data)
    {
        Structure structure = null;

        try
        {
            if (data != null) {
                ByteArrayInputStream byteArray = new ByteArrayInputStream(data);
                ObjectInputStream obj = new ObjectInputStream(byteArray);
                structure = (Structure) obj.readObject();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return structure;
    }
}
