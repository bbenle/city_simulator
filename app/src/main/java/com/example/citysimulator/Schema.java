package com.example.citysimulator;

// schema for the databases
// used to store the names so there won't be any typos
public class Schema
{
    // settings
    public static class SettingsTable
    {
        public static final String NAME = "settings";

        public static class Cols
        {
            public static final String ID = "ID";
            public static final String HEIGHT = "mapHeight";
            public static final String WIDTH = "mapWidth";
            public static final String INITMONEY = "initialMoney";
        }
    }

    // game data
    public static class GameDataTable
    {
        public static final String NAME = "gameData";

        public static class Cols
        {
            public static final String MONEY = "money";
            public static final String TIME = "gameTime";
            public static final String POP = "population";
            public static final String INCOME = "income";
            public static final String EMP = "employment";
            public static final String NRES = "residential";
            public static final String NCOMM = "commercial";
            public static final String ID = "ID";
        }
    }

    // game element
    public static class GameElementTable
    {
        public static final String NAME = "gameElement";

        public static class Cols
        {
            public static final String STRUCTURE = "structure";
            public static final String OWNERNAME = "ownerName";
            public static final String R = "r";
            public static final String C = "c";
        }
    }
}
