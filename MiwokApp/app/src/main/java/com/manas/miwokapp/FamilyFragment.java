package com.manas.miwokapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyFragment extends Fragment {

    public FamilyFragment() { }
    MediaPlayer m;
    private AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                m.pause();
                m.seekTo(0);
            }
            else if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
                m.start();
            }
            else if(focusChange==AudioManager.AUDIOFOCUS_LOSS){
                releaseMediaPlayer();
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview= inflater.inflate(R.layout.list, container, false);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("father", "papa", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "ma", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "beta", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "beti", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("grandmother", "dadi", R.drawable.family_grandfather, R.raw.family_grandmother));
        words.add(new Word("grandfather", "dada", R.drawable.family_grandfather, R.raw.family_grandfather));
        words.add(new Word("sister-in-law", "nand", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("brother-in-law", "jiju", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("older brother", "bhaiya", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("older sister", "didi", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger brother", "bhai", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("younger sister", "bhen", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("wife", "biwi", R.drawable.family_older_sister, R.raw.family_older_sister));

        audioManager= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        WordAdapter a = new WordAdapter(getActivity(), words, R.color.category_family);
        ListView l1 = (ListView) rootview.findViewById(R.id.list);
        l1.setAdapter(a);
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);
                releaseMediaPlayer();
                int result=audioManager.requestAudioFocus(audioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    m = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                    m.start();
                    m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            releaseMediaPlayer();
                        }
                    });
                }
            }
        });

        return rootview;
    }
    public void onStop(){
        super.onStop();
        releaseMediaPlayer();
    }
    private void releaseMediaPlayer() {
        if (m != null) {
            m.release();
            m = null;
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }
}
