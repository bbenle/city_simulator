package com.example.citysimulator;

public class Settings
{
    // private class fields
    // database class fields
    private int mapHeight;
    private int mapWidth;
    private int initialMoney;
    private int salary;

    // constant class fields
    private int familySize = 4;
    private int shopSize = 6;
    private int serviceCost = 2;
    private int houseBuildingCost = 100;
    private int commBuildingCost = 500;
    private int roadBuildingCost = 20;
    private double taxRate = 0.3;
    private int ID = 0;

    // public constructor
    public Settings()
    {
        mapHeight = 10;
        mapWidth = 50;
        initialMoney = 1000;
        familySize = 4;
        shopSize = 6;
        salary = 10;
        taxRate = 0.3;
        serviceCost = 2;
        houseBuildingCost = 100;
        commBuildingCost = 500;
        roadBuildingCost = 20;
    }

    // alternate constructor -> used for SettingsCursor
    public Settings(int height, int width, int initialMoney,int id)
    {
        this.mapHeight = height;
        this.mapWidth = width;
        this.initialMoney = initialMoney;
        this.ID = id;
    }

    // GETTERS
    public int getMapWidth()
    {
        return mapWidth;
    }

    public int getMapHeight()
    {
        return mapHeight;
    }

    public int getInitialMoney()
    {
        return initialMoney;
    }

    public int getFamilySize()
    {
        return familySize;
    }

    public int getID(){return  ID;}

    public int getShopSize()
    {
        return shopSize;
    }

    public int getSalary()
    {
        return salary;
    }

    public double getTaxRate()
    {
        return taxRate;
    }

    public int getServiceCost()
    {
        return serviceCost;
    }

    public int getHouseBuildingCost()
    {
        return houseBuildingCost;
    }

    public int getCommBuildingCost()
    {
        return commBuildingCost;
    }

    public int getRoadBuildingCost()
    {
        return roadBuildingCost;
    }

    // SETTERS
    public void setMapWidth(int mapWidth)
    {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight)
    {
        this.mapHeight = mapHeight;
    }
}
