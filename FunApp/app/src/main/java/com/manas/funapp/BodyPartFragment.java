package com.manas.funapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.manas.funapp.data.AndroidImageAssests;

import java.util.ArrayList;
import java.util.List;

public class BodyPartFragment extends Fragment {
    private List<Integer> mImageIds;
    private int mListIndex;

    public  static final String IMAGE_ID_LIST="image_ids";
    public  static final String LIST_INDEX="list_index";

    public BodyPartFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState!=null){
            mImageIds=savedInstanceState.getIntegerArrayList(IMAGE_ID_LIST);
            mListIndex=savedInstanceState.getInt(LIST_INDEX);
        }
        View rootView= inflater.inflate(R.layout.fragment_body_part, container, false);
        final ImageView i=(ImageView)rootView.findViewById(R.id.body_part_image_view);

        if(mImageIds!=null) {
            i.setImageResource(mImageIds.get(mListIndex));
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListIndex<=mImageIds.size()-1)
                        mListIndex++;
                    else
                        mListIndex=0;
                    i.setImageResource(mImageIds.get(mListIndex));
                }
            });
        }
        return rootView;
    }

    public void setImageIds(List<Integer> imageIds){
        mImageIds=imageIds;
    }
    public void setListIndex(int listIndex)
    {
        mListIndex=listIndex;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putIntegerArrayList(IMAGE_ID_LIST,(ArrayList<Integer>) mImageIds);
        currentState.putInt(LIST_INDEX,(int) mListIndex);
    }
}

