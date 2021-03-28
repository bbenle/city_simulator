package com.example.citysimulator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.Toast;

import com.example.citysimulator.Schema.SettingsTable;
import com.example.citysimulator.Schema.GameDataTable;
import com.example.citysimulator.Schema.GameElementTable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class GameData
{
    // private class fields
    private SQLiteDatabase db; // the database connection
    private Settings settings;
    private GameElement[][] grid;
    private GameData gameData;

    // private class fields for the GameData database/Status Fragment
    private String cityName;
    private int gameTime;
    private int money;
    private int population;
    private double income;
    private double employment;

    // residential and commercial count
    private int nResidential;
    private int nCommercial;

    private boolean gameOver = false;
    private int id = 1;

    private static GameData instance = null;

    public static GameData get()
    {
        if (instance == null)
        {
            instance = new GameData();
        }
        return instance;
    }

    // method to set the instance to null - used in resetButton in status fragment
    public void resetGameData()
    {
        instance = null;
    }

    // method to generate the grid
    private GameElement[][] generateGrid()
    {
        // generates the grid based on the map height and map width from settings
        GameElement[][] grid = new GameElement[settings.getMapHeight()][settings.getMapWidth()];

        // double for loop to create a 2D array for the map
        for (int ii = 0; ii < settings.getMapHeight(); ii++)
        {
            for (int jj = 0; jj <settings.getMapWidth(); jj++)
            {
                grid[ii][jj] = new GameElement(null, " ", ii, jj);
            }
        }
        return  grid;
    }

    // default constructor
    private GameData()
    {
        settings = new Settings();
        cityName = "Perth";
        gameTime = 0;
        money = settings.getInitialMoney();
        population = 0;
        income = 0;
        employment = 0;
        gameOver = false;
    }

    // alternate constructor -> used for GameDataCursor
    public GameData(int money, int time, int population, double income, double employment, int nResidential, int nCommercial, int id)
    {
        this.money = money;
        this.gameTime = time;
        this.population = population;
        this.income = income;
        this.employment = employment;
        this.nResidential = nResidential;
        this.nCommercial = nCommercial;
        this.id = id;
    }

    // method to set the grid to a newly generated grid
    public void regenerate()
    {
        this.grid = generateGrid();
    }

    public GameElement get(int i, int j)
    {
        return grid[i][j];
    }

    // GAME ELEMENT DATABASE
    // method to load the gameElement database
    public void loadGameElement(Context context)
    {
        this.db = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();

        int width = settings.getMapWidth();
        int height = settings.getMapHeight();

        GameElement[][] map;

        // executes the query
        GameElementCursor cursor = new GameElementCursor(db.query(GameElementTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null));

        // iterating over the query results
        try
        {
            if (cursor.getCount() != 0) // if cursorCount is not 0
            {
                cursor.moveToFirst(); // move to first
                map = new GameElement[height][width]; // create map based on height and width

                while (!cursor.isAfterLast())
                {
                    GameElement gameElement = cursor.getGameElement(); // sets gameElement to be the gameElement in cursor
                    map[gameElement.getRow()][gameElement.getCol()] = gameElement; // creates the map based on what gameElement is from above
                    cursor.moveToNext();
                }
                grid = map;
            }
            else
            {
                int ii, jj;

                regenerate(); // generate grid

                // for loops for the 2D array
                for (ii = 0; ii < height; ii++)
                {
                    for (jj = 0; jj < width; jj++)
                    {
                        GameElement gameElement = grid[ii][jj];

                        // adding these elements to the database
                        ContentValues cv = new ContentValues();
                        cv.put(GameElementTable.Cols.OWNERNAME, gameElement.getOwnerName());
                        cv.put(GameElementTable.Cols.STRUCTURE, makebyte(gameElement.getStructure()));
                        cv.put(GameElementTable.Cols.R, gameElement.getRow());
                        cv.put(GameElementTable.Cols.C, gameElement.getCol());

                        // insert
                        db.insert(GameElementTable.NAME, null, cv);
                    }
                }
            }
        }
        finally
        {
            cursor.close();
        }
    }

    // method to add to the database
    public void addGameElement(int row, int col)
    {
        GameElement gameElement = grid[row][col];

        ContentValues cv = new ContentValues();
        cv.put(GameElementTable.Cols.OWNERNAME, gameElement.getOwnerName());
        cv.put(GameElementTable.Cols.STRUCTURE, makebyte(gameElement.getStructure()));
        cv.put(GameElementTable.Cols.R, gameElement.getRow());
        cv.put(GameElementTable.Cols.C, gameElement.getCol());

        String[] whereValue = {String.valueOf(gameElement.getRow()), String.valueOf(gameElement.getCol())};

        db.update(GameElementTable.NAME, cv,GameElementTable.Cols.R + " = ? AND " + GameElementTable.Cols.C + " = ?" , whereValue);
    }

    // method to convert object to a byte array and store it in database as as blob
    // this code was obtained from this source https://stackoverflow.com/questions/1243181/how-to-store-object-in-sqlite-database
    public byte[] makebyte(Structure structure)
    {
        byte[] employeeAsBytes = null;

        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(structure);
            employeeAsBytes = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(employeeAsBytes);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return employeeAsBytes;
    }

    // GAME DATA DATABASE
    // method to load the gameData database
    public void loadGameData(Context context)
    {
        this.db = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();

        // executes the query
        GameDataCursor cursor = new GameDataCursor(db.query(Schema.GameDataTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null));

        // iterating over the query results
        try
        {
            // if cursorCount is 0, add these variables to database
            if (cursor.getCount() == 0)
            {
                ContentValues cv = new ContentValues();
                cv.put(GameDataTable.Cols.ID, id);
                cv.put(GameDataTable.Cols.MONEY, money);
                cv.put(GameDataTable.Cols.TIME, gameTime);
                cv.put(GameDataTable.Cols.POP, population);
                cv.put(GameDataTable.Cols.INCOME, income);
                cv.put(GameDataTable.Cols.EMP, employment);
                cv.put(GameDataTable.Cols.NRES, nResidential);
                cv.put(GameDataTable.Cols.NCOMM, nCommercial);

                db.insert(GameDataTable.NAME, null, cv);
            }
            else // else set the variables to be whatever the gameData value is
            {
                cursor.moveToFirst();
                gameData = cursor.getGameData();

                id = gameData.getId();
                money = gameData.getMoney();
                gameTime = gameData.getGameTime();
                population = gameData.getPopulation();
                income = gameData.getIncome();
                employment = gameData.getEmployment();
                nResidential = gameData.getResidentialCount();
                nCommercial = gameData.getCommercialCount();
            }
        }
        finally
        {
            cursor.close();
        }
    }

    // method to add to the database
    public void addGameData()
    {
        ContentValues cv = new ContentValues();
        cv.put(GameDataTable.Cols.ID, id);
        cv.put(GameDataTable.Cols.MONEY, money);
        cv.put(GameDataTable.Cols.TIME, gameTime);
        cv.put(GameDataTable.Cols.POP, population);
        cv.put(GameDataTable.Cols.INCOME, income);
        cv.put(GameDataTable.Cols.EMP, employment);
        cv.put(GameDataTable.Cols.NRES, nResidential);
        cv.put(GameDataTable.Cols.NCOMM, nCommercial);

        db.update(GameDataTable.NAME, cv, "ID = ?", new String[] {"1"});
    }

    // SETTINGS DATABASE
    // method to load the settings database
    public void loadSettings(Context context) {
        this.db = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();

        // executes the query
        SettingsCursor cursor = new SettingsCursor(db.query(Schema.SettingsTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null));

        // if cursorCount is 0, create new settings and add to the database
        if (cursor.getCount() == 0)
        {
            settings = new Settings();
            addSettings(settings);
        }

        // iterating over the query results
        try {
            if (cursor.moveToFirst()) {
                do {
                    cursor.getSettings();
                    this.settings = cursor.getSettings();
                }
                while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
    }

    // method to add to the database
    public void addSettings(Settings settings)
    {
        ContentValues cv = new ContentValues();
        cv.put(SettingsTable.Cols.ID, settings.getID());
        cv.put(SettingsTable.Cols.HEIGHT, settings.getMapHeight());
        cv.put(SettingsTable.Cols.WIDTH, settings.getMapWidth());
        cv.put(SettingsTable.Cols.INITMONEY, settings.getInitialMoney());

        db.insert(SettingsTable.NAME, null, cv);
    }

    // method to drop the all the tables and load them in again
    public void dropTables(Context context)
    {
        db.delete(GameDataTable.NAME, null, null); // deletes the gameData table
        db.delete(GameElementTable.NAME, null, null); // deletes the gameElement table
        db.delete(SettingsTable.NAME, null, null); // deletes the settings table

        loadGameData(context); // load gameData database
        loadGameElement(context); // load gameElement database
        addSettings(settings); // update settings database

    }

    // method to delete all databases and set instance to null -> used in
    public void deleteDatabase()
    {
        db.delete(SettingsTable.NAME, null, null); // deletes the settings table
        db.delete(GameDataTable.NAME, null, null); // deletes gameData table
        db.delete(GameElementTable.NAME, null, null); // deletes gameElement table

        instance = null;
        get();
    }

    // method to generate grid
    public void regenerateGrid() {
        grid = generateGrid();
    }

    // method to check if a grid square has a road adjacent to it. if it does, that tile is buildable
    public boolean checkRoadAdjacent(int row, int col, Context context, Structure structure)
    {
        boolean buildable = false;

        // up check
        if ((settings.getMapWidth() > col+1)) // if width greater than 1 tile north of that grid square
        {
            if (grid[row][col+1].getStructure() instanceof Road) // if that northern tile is a road
            {
                buildable = true;
            }
        }

        // down check
        if ((settings.getMapWidth() > col-1) && (!(col-1 < 0))) // if width greater than 1 tile south and if it is not less than 0
        {
            if (grid[row][col-1].getStructure() instanceof Road) // if that southern tile is a road
            {
                buildable = true;
            }
        }

        // right check
        if ((settings.getMapHeight() > row+1)) // if height greater than 1 tile east of that grid square
        {
            if (grid[row+1][col].getStructure() instanceof Road)  // if that eastern tile is a road
            {
                buildable = true;
            }
        }

        // left check
        if ((settings.getMapHeight() > row-1) && (!(row-1 < 0))) // if height greater than 1 tile west and if it is not less than 0
        {
            if (grid[row-1][col].getStructure() instanceof Road) // if that western square is a road
            {
                buildable = true;
            }
        }

        // if it is not buildable and if the structure is not a road, it will display the toast message
        // toast message only appears if user tries to build a structure that is not adjacent to a road
        if ((!buildable) && !(structure instanceof Road))
        {
            Toast toast = Toast.makeText(context, "Must be built next to a road!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();
        }
        return buildable;
    }

    // method that changes values based on formulas from the assignment spec. whenever time is increased by a unit, it changes
    public void increaseTime(Context context)
    {
        // formulas for increasing money with each unit of time being increased
        population = settings.getFamilySize() * nResidential;

        if (population != 0)
        {
            employment = Math.min(1, nCommercial * settings.getShopSize() / (double) population); // cap this to 3 decimals
        }

        // income and money
        income = population * (employment * settings.getSalary() * settings.getTaxRate() - settings.getServiceCost());
        money += income;

        gameTime++; // increases gameTime count by 1
        gameOver(context); // checks if the game is lost
        addGameData(); // add to gameData database
    }

    // method to updateStructureImage
    public void updateStructureImage(int row, int column, String name, Bitmap image)
    {
        if (image != null)
        {
            grid[column][row].setBitmapImage(image);

        }
        grid[column][row].setOwnerName(name);
    }

    // method to check if game is over
    public boolean gameOver(Context context)
    {
        // if money is less than or equal to 0, player loses
        // sets gameOver boolean to true and send a toast
        if (GameData.get().getMoney() <= 0)
        {
           gameOver = true;

            Toast toast = Toast.makeText(context, "GAME OVER!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();
        }
        return gameOver;
    }

    // GETTERS
    public Settings getSettings()
    {
        return settings;
    }

    public GameElement[][] getMapElement()
    {
        return grid;
    }

    public int getMoney()
    {
        return money;
    }

    public int getGameTime()
    {
        return gameTime;
    }

    public String getCityName() {
        return cityName;
    }

    public int getPopulation() {
        return population;
    }

    public double getIncome()
    {
        return income;
    }

    public double getEmployment() {
        return employment;
    }

    public int getResidentialCount() {
        return nResidential;
    }

    public int getCommercialCount() {
        return nCommercial;
    }

    public int getId() {
        return id;
    }


    // SETTERS
    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }

    public void setMoney(int money)
    {
        this.money = money;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    // increase residential count
    public void increaseResCount() {
        nResidential++;
    }

    // increase commercial count
    public void increaseCommCount() {
        nCommercial++;
    }

    // decrease residential count
    public void decreaseResCount()
    {
        nResidential--;
    }

    // increase commercial count
    public void decreaseCommCount()
    {
        nCommercial--;
    }
}
