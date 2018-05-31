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

public class PhraseFragment extends Fragment {

    public PhraseFragment() { }
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
    private void releaseMediaPlayer() {
        if (m != null) {
            m.release();
            m = null;
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview= inflater.inflate(R.layout.list, container, false);

        final ArrayList<Word> words=new ArrayList<Word>();
        words.add(new Word("Where are you going ?","Tum kaha ja rhe ho ?",R.raw.phrase_come_here));
        words.add(new Word("What is your name ?","Tumhara kya naam hai ?",R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is ...","Mera naam ... hai .",R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling ?","Tumhe kaisa lag rha hai ?",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("How much money do you want","Tumhe kitne rupee chaheye ?",R.raw.phrase_yes_im_coming));
        words.add(new Word("Lets's go","Chalo chale",R.raw.phrase_lets_go));
        words.add(new Word("Come here","Yha aao",R.raw.phrase_come_here));
        words.add(new Word("What do you want?","Tumhe kya chaheye ?",R.raw.phrase_yes_im_coming));
        words.add(new Word("Are you hungry","Kya tum bhukhe ho ?",R.raw.phrase_are_you_coming));
        words.add(new Word("I m feeling hungry.","Muje bhuj lag rhi hai .",R.raw.phrase_im_feeling_good));

        audioManager= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        WordAdapter a=new WordAdapter(getActivity(),words,R.color.category_phrases);
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
}
