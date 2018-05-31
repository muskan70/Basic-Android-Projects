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

public class ColorFragment extends Fragment {

    public ColorFragment() {}
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

        final ArrayList<Word> words=new ArrayList<Word>();
        words.add(new Word("red","laal",R.drawable.color_red,R.raw.color_red));
        words.add(new Word("yellow","peela",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));
        words.add(new Word("dusty-yellow","matmella",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        words.add(new Word("dark-red","mehroon",R.drawable.color_red,R.raw.color_red));
        words.add(new Word("brown","bhura",R.drawable.color_brown,R.raw.color_brown));
        words.add(new Word("gray","kamkala",R.drawable.color_gray,R.raw.color_gray));
        words.add(new Word("green","hra",R.drawable.color_green,R.raw.color_green));
        words.add(new Word("white","safed",R.drawable.color_white,R.raw.color_white));
        words.add(new Word("black","kala",R.drawable.color_black,R.raw.color_black));

        audioManager= (AudioManager)getActivity(). getSystemService(Context.AUDIO_SERVICE);
        WordAdapter a=new WordAdapter(getActivity(),words,R.color.category_colors);
        ListView l1=(ListView)rootview.findViewById(R.id.list);
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
