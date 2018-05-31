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

public class NumberFragment extends Fragment {
    public  NumberFragment(){}

    private MediaPlayer m;
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

        View rootview=inflater.inflate(R.layout.list, container, false);

        final ArrayList<Word> words=new ArrayList<Word>();
        words.add(new Word("one","ek",R.drawable.number_one,R.raw.number_one));
        words.add(new Word("two","do",R.drawable.number_two,R.raw.number_two));
        words.add(new Word("three","teen",R.drawable.number_three,R.raw.number_three));
        words.add(new Word("four","char",R.drawable.number_four,R.raw.number_four));
        words.add(new Word("five","paach",R.drawable.number_five,R.raw.number_five));
        words.add(new Word("six","cheh",R.drawable.number_six,R.raw.number_six));
        words.add(new Word("seven","saat",R.drawable.number_seven,R.raw.number_seven));
        words.add(new Word("eight","aath",R.drawable.number_eight,R.raw.number_eight));
        words.add(new Word("nine","no",R.drawable.number_nine,R.raw.number_nine));
        words.add(new Word("ten","dus",R.drawable.number_ten,R.raw.number_ten));

        audioManager= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        WordAdapter a=new WordAdapter(getActivity(),words,R.color.category_numbers);
        ListView l1=(ListView)rootview.findViewById(R.id.list);
        l1.setAdapter(a);
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word=words.get(position);
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
                                              }
                    );
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
