package com.example.citysimulator;

import android.graphics.Bitmap;

public class GameElement
{
    // private class fields
    private Structure structure;
    private Bitmap image = null;
    private String ownerName;

    private int row;
    private int column;

    // alternate constructor
    public GameElement(Structure structure, String ownerName, int row, int column)
    {
        this.ownerName = ownerName;
        this.structure = structure;
        this.row = row;
        this.column = column;
    }

    // check if there is a structure placed, returns true if structure is null
    public boolean checkStructure()
    {
        return structure == null;
    }

    // method to check if an image exists (used to display image in details screen)
    // returns true if the image is not null
    public boolean checkImageExistance()
    {
        return image != null;
    }

    // GETTERS
    public Structure getStructure()
    {
        return structure;
    }

    public Bitmap getBitMapImage()
    {
        return image;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return column;
    }

    // SETTERS
    public void setStructure(Structure structure)
    {
        this.structure = structure;

        if (structure == null)
        {
            this.structure = null;
        }
    }

    public void setBitmapImage(Bitmap image)
    {
        this.image = image;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }
}
