package com.example.citysimulator;

import java.io.Serializable;

// abstract super class
public abstract class Structure implements Serializable
{
    // private class fields
    private final int drawableId;
    private String label;

    // alternate constructor
    public Structure(int drawableId, String label)
    {
        this.drawableId = drawableId;
        this.label = label;
    }

    // GETTERS
    public int getDrawableId()
    {
        return drawableId;
    }

    public String getLabel()
    {
        return label;
    }
}
