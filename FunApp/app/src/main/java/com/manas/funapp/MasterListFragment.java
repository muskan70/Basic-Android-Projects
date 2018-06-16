package com.manas.funapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.manas.funapp.data.AndroidImageAssests;

// The list appears as a grid of images
public class MasterListFragment extends Fragment {

    public interface OnImageClickListener {
        void onImageSelected(int position);
    }

    OnImageClickListener callback;

    // Mandatory empty constructor
    public MasterListFragment() {
    }

    public void onAttach(Context context){
        super.onAttach(context);

        try{
            callback=(OnImageClickListener)context;
        }catch(ClassCastException e){
                throw new ClassCastException(context.toString() +"cant implement OnImageClickListner");
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        // Get a reference to the GridView in the fragment_master_list xml layout file
        GridView gridView = (GridView) rootView.findViewById(R.id.images_grid_view);

        MasterListAdapter mAdapter = new MasterListAdapter(getContext(), AndroidImageAssests.getAll());
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        callback.onImageSelected(position);
                    }
                });

        // Return the root view
        return rootView;
    }

}
