package com.example.citysimulator;

import java.util.Arrays;
import java.util.List;

public class StructureData
{
    // array containing the drawable ID for the structures
    public static final int[] DRAWABLES = {
            0, R.drawable.ic_building1, R.drawable.ic_building2, R.drawable.ic_building3,
            R.drawable.ic_building4, R.drawable.ic_building5, R.drawable.ic_building6,
            R.drawable.ic_building7, R.drawable.ic_building8,
            R.drawable.ic_road_n, R.drawable.ic_road_ne, R.drawable.ic_road_new,
            R.drawable.ic_road_ns, R.drawable.ic_road_nse, R.drawable.ic_road_nsew,
            R.drawable.ic_road_nsw, R.drawable.ic_road_nw,
            R.drawable.ic_road_e, R.drawable.ic_road_ew,
            R.drawable.ic_road_s, R.drawable.ic_road_se, R.drawable.ic_road_sew,
            R.drawable.ic_road_sw, R.drawable.ic_road_w};

    private List<Structure> structureList = Arrays.asList(
            // residential structures
            new Residential(R.drawable.ic_building1, "Res1"),
            new Residential(R.drawable.ic_building2, "Res2"),
            new Residential(R.drawable.ic_building3, "Res3"),
            new Residential(R.drawable.ic_building4, "Res4"),

            // commercial structures
            new Commercial(R.drawable.ic_building5, "Comm1"),
            new Commercial(R.drawable.ic_building6, "Comm2"),
            new Commercial(R.drawable.ic_building7, "Comm3"),
            new Commercial(R.drawable.ic_building8, "Comm4"),

            // roads
            new Road(R.drawable.ic_road_n, "Road1"),
            new Road(R.drawable.ic_road_ne, "Road2"),
            new Road(R.drawable.ic_road_new, "Road3"),
            new Road(R.drawable.ic_road_ns, "Road4"),
            new Road(R.drawable.ic_road_nse, "Road5"),
            new Road(R.drawable.ic_road_nsew, "Road6"),
            new Road(R.drawable.ic_road_nsw, "Road7"),
            new Road(R.drawable.ic_road_nw, "Road8"),
            new Road(R.drawable.ic_road_e, "Road9"),
            new Road(R.drawable.ic_road_ew, "Road10"),
            new Road(R.drawable.ic_road_s, "Road11"),
            new Road(R.drawable.ic_road_se, "Road12"),
            new Road(R.drawable.ic_road_sew, "Road13"),
            new Road(R.drawable.ic_road_sw, "Road14"),
            new Road(R.drawable.ic_road_w, "Road15"));

    private static StructureData instance = null;

    public static StructureData get()
    {
        if(instance == null)
        {
            instance = new StructureData();
        }
        return instance;
    }

    protected StructureData() {}

    public int size()
    {
        return structureList.size();
    }


    // GETTERS
    public Structure get(int i)
    {
        return structureList.get(i);
    }
}