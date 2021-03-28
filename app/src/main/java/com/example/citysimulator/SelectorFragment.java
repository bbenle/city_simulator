package com.example.citysimulator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectorFragment extends Fragment
{
    // private class fields
    private LinearLayoutManager llm;
    private StructureData sd;
    private SelectorAdapter adapter;
    private Structure selectedStructure;

    // called when the activity starts and is where most initialisation goes
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        sd = StructureData.get();
        adapter = new SelectorAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.selector_fragment, ui, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.selectorRecyclerView);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        return view;
    }

    // adapter
    private class SelectorAdapter extends RecyclerView.Adapter<SelectorViewHolder>
    {
        @Override
        public int getItemCount() {
            return sd.size();
        }

        @Override
        public SelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new SelectorViewHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(SelectorViewHolder vh, int index)
        {
            vh.bind(sd.get(index));
        }
    }

    // viewholder
    private class SelectorViewHolder extends RecyclerView.ViewHolder
    {
        // private class fields
        private ImageView structureImage;
        private TextView structureText;

        public SelectorViewHolder(LayoutInflater li, ViewGroup parent)
        {
            super(li.inflate(R.layout.structure_selection, parent, false));

            structureImage = (ImageView) itemView.findViewById(R.id.structureImage);
            structureText = (TextView) itemView.findViewById(R.id.structureText);

            // when the user clicks a structure in the selector, it will set the current selected structure
            structureImage.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    selectedStructure = sd.get(getAdapterPosition());
                }
            });
        }

        public void bind(Structure structure)
        {
            structureImage.setImageResource(structure.getDrawableId());
            structureText.setText(structure.getLabel());
        }
    }

    // GETTERS
    public Structure getSelectedStructure()
    {
        return selectedStructure;
    }
}