package com.example.citysimulator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MapFragment extends Fragment
{
    // private class fields
    private Settings settings;
    private StatusFragment statusFragment;
    private GridLayoutManager glm;
    private SelectorFragment selector;
    private GameData gameData;
    private MapAdapter adapter;

    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_DETAILS = 0;

    // called when the activity starts and is where most initialisation goes
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        settings = GameData.get().getSettings();
        glm = new GridLayoutManager(getActivity(), settings.getMapHeight(), GridLayoutManager.HORIZONTAL, false);
        gameData = GameData.get();
        adapter = new MapAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.map_fragment, ui, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.mapRecyclerView);
        rv.setLayoutManager(glm);
        rv.setAdapter(adapter);
        return view;
    }

    // adapter
    private class MapAdapter extends RecyclerView.Adapter<MapViewHolder>
    {
        @Override
        public MapViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new MapViewHolder(li, parent);
        }

        @Override
        public int getItemCount()
        {
            return settings.getMapHeight() * settings.getMapWidth();
        }

        @Override
        public void onBindViewHolder(MapViewHolder vh, int index)
        {
            // translating rows and columns to positions
            int row = index % settings.getMapHeight();
            int column = index / settings.getMapHeight();
            vh.bind(gameData.get(row, column));
        }
    }

    // viewholder
    private class MapViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView nw;
        private ImageView ne;
        private ImageView se;
        private ImageView sw;
        private ImageView bigImageView;
        private GameElement gameElement;

        public MapViewHolder(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.map_grid, parent, false));
            int size = parent.getMeasuredHeight() / settings.getMapHeight() + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;

            nw = (ImageView) itemView.findViewById(R.id.northwest);
            ne = (ImageView) itemView.findViewById(R.id.northeast);
            se = (ImageView) itemView.findViewById(R.id.southeast);
            sw = (ImageView) itemView.findViewById(R.id.southwest);
            bigImageView = (ImageView) itemView.findViewById(R.id.bigImageView);

            // click on the map to build the structures
            bigImageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Structure selectedStructure = selector.getSelectedStructure();

                    // build
                    // checks if the structure is not null
                    // if demolish button is pressed, if details button is pressed
                    if ((gameElement.checkStructure() && !statusFragment.demolishPressed() && !statusFragment.detailsPressed()))
                    {
                        // if the tiles adjacent to a road is buildable (from checkRoadAdjacent method) and is an instance of Road
                        if (GameData.get().checkRoadAdjacent(getRow(getAdapterPosition()), getColumn(getAdapterPosition()), getContext(), selectedStructure) || selectedStructure instanceof Road)
                        {
                            // residential building
                            if ((selectedStructure instanceof Residential)) {
                                int cost = settings.getHouseBuildingCost();

                                // if money is greater or equal to cost of the residential building, can build it
                                if (GameData.get().getMoney() >= cost) {
                                    gameElement.setStructure(selectedStructure);
                                    GameData.get().setMoney(GameData.get().getMoney() - cost);
                                    GameData.get().increaseResCount(); // increases residential count
                                    statusFragment.updateStats();
                                }
                                else // else send toast stating not enough money
                                {
                                    printNotEnoughMoney(getContext());
                                }
                            }
                            // commercial building
                            else if (selectedStructure instanceof Commercial) {
                                int cost = settings.getCommBuildingCost();

                                // if money is greater or equal to cost of the commerical building, can build it
                                if ((GameData.get().getMoney() >= cost) && (gameElement != null)) {
                                    gameElement.setStructure(selectedStructure);
                                    GameData.get().setMoney(GameData.get().getMoney() - cost);
                                    GameData.get().increaseCommCount(); // increases commercial count
                                    System.out.println(GameData.get().getCommercialCount());
                                    statusFragment.updateStats();
                                }
                                else // else send toast stating not enough money
                                {
                                    printNotEnoughMoney(getContext());
                                }
                            }
                            // road building
                            else if (selectedStructure instanceof Road) {
                                int cost = settings.getRoadBuildingCost();

                                // if money is greater or equal to cost of the road, can build it
                                if (GameData.get().getMoney()  >= cost) {
                                    gameElement.setStructure(selectedStructure);
                                    GameData.get().setMoney(GameData.get().getMoney()  - cost);
                                    statusFragment.updateStats();
                                }
                                else // else send toast stating not enough money
                                {
                                    printNotEnoughMoney(getContext());
                                }
                            }
                        }
                        GameData.get().addGameData();
                    }
                    // demolish residential
                    else if (statusFragment.demolishPressed() && gameElement.getStructure() instanceof Residential)
                    {
                        // sets structure, bitmapImage and ownerName to be null
                        gameElement.setStructure(null);
                        gameElement.setBitmapImage(null);
                        gameElement.setOwnerName(null);
                        GameData.get().decreaseResCount(); // decrease residential count by 1

                    }
                    // demolish commercial
                    else if (statusFragment.demolishPressed() && gameElement.getStructure() instanceof Commercial)
                    {
                        // sets structure, bitmapImage and ownerName to be null
                        gameElement.setStructure(null);
                        gameElement.setBitmapImage(null);
                        gameElement.setOwnerName(null);
                        GameData.get().decreaseCommCount(); // decrease commercial count by 1
                    }
                    // demolish road
                    else if (statusFragment.demolishPressed() && gameElement.getStructure() instanceof Road)
                    {
                        // sets structure, bitmapImage and ownerName to be null
                        gameElement.setStructure(null);
                        gameElement.setBitmapImage(null);
                        gameElement.setOwnerName(null);
                    }
                    // details button
                    else if (statusFragment.detailsPressed())
                    {
                        // check if a structure exists, otherwise if user clicks on empty map, it will crash
                        if (!gameElement.checkStructure())
                        {
                            // passing through image, name, row, column and drawable id for the details screen
                            Intent intent = new Intent(getActivity(), DetailsScreen.class);
                            intent.putExtra("bitmap", gameElement.getBitMapImage());
                            //intent.putExtra("name", gameElement.getStructure().getLabel());
                            intent.putExtra("name", gameElement.getOwnerName());
                            intent.putExtra("row", getRow(getAdapterPosition()));
                            intent.putExtra("column", getColumn(getAdapterPosition()));
                            intent.putExtra("drawableID", gameElement.getStructure().getDrawableId());

                            startActivityForResult(intent, REQUEST_CODE);
                            statusFragment.setDetailsPressed(false); // sets details pressed back to false
                        }
                    }
                    GameData.get().addGameData();
                    GameData.get().addGameElement(gameElement.getRow(), gameElement.getCol());
                    adapter.notifyItemChanged(getAdapterPosition()); // refreshes screen when something changed
                }
            });
        }

        public void bind(GameElement gameElement)
        {
            ne.setImageResource(R.drawable.ic_grass3);
            nw.setImageResource(R.drawable.ic_grass3);
            se.setImageResource(R.drawable.ic_grass3);
            sw.setImageResource(R.drawable.ic_grass3);

            // checks if the structure is not null
            if (gameElement.getStructure() != null)
            {
                // checks if the image actually exists, if it exists, sets the Bitmap image
                if (gameElement.checkImageExistance())
                {
                    bigImageView.setImageBitmap(gameElement.getBitMapImage()); // sets the Bitmap image
                }
                else
                {
                    bigImageView.setImageResource(gameElement.getStructure().getDrawableId());
                }
            }
            else
            {
                bigImageView.setImageResource(0);
            }
            this.gameElement = gameElement;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnData)
    {
        if (resultCode == RESULT_OK && requestCode == REQUEST_DETAILS)
        {
            Bitmap image = (Bitmap) returnData.getParcelableExtra("image");
            int row = returnData.getIntExtra("row", 0);
            int column = returnData.getIntExtra("column", 0);
            String name = returnData.getStringExtra("name");
            GameElement gameElement = GameData.get().get(row,column);

            if(image != null)
            {
                gameElement.setBitmapImage(image);
            }
            gameElement.setOwnerName(name);
            adapter.notifyDataSetChanged();
        }
    }

    // method to send a toast if user does not have enough money
    public void printNotEnoughMoney(Context context)
    {
        Toast toast = Toast.makeText(context, "Not enough money!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
        toast.show();
    }

    // GETTERS
    public int getRow(int index)
    {
        return index % settings.getMapHeight();
    }

    public int getColumn(int index)
    {
        return index / settings.getMapHeight();
    }

    // SETTERS
    public void setSelectorFragment(SelectorFragment selector)
    {
        this.selector = selector;
    }

    public void setStatusFragment(StatusFragment statusFragment)
    {
        this.statusFragment = statusFragment;
    }
}