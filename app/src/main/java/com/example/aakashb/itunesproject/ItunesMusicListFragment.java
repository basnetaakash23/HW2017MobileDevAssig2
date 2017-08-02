package com.example.aakashb.itunesproject;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ItunesMusicListFragment extends Fragment {
    private ListView mListView;
    private MusicAdapter mMusicAdapter;
    private TextView mSongTitleView;
    private TextView mTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NetworkImageView imageView;
    private List<Music> mMusics;
    private MediaPlayer mMediaPlayer;
    private String mCurrentlyPlayingUrl;
    private ImageButton mImageButton;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


    }

    private void clickedAudioURL(String url) {
        if (mMediaPlayer.isPlaying()) {
            if (mCurrentlyPlayingUrl.equals(url)) {
                mMediaPlayer.stop();
                mMusicAdapter.notifyDataSetChanged();
                return;
            }
        }

        mCurrentlyPlayingUrl = url;
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mMusicAdapter.notifyDataSetChanged();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMusicAdapter.notifyDataSetChanged();
            }
        });
    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_itunesarticlelist,container,false);

        //setting up the SwipeRefreshLayout to reload when we swipe
        mListView = (ListView) v.findViewById(R.id.list_view);
        mMusicAdapter = new MusicAdapter(getActivity());
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMusic();
            }
        });


        //Setting up the list view

        mListView.setAdapter( mMusicAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Music music = (Music) parent.getAdapter().getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(music.getTrackViewUrl()));
                startActivity(intent);
            }

        });

        if (mMusics != null) {
            mMusicAdapter.setItems(mMusics);

        } else {
            mSwipeRefreshLayout.setRefreshing(true);
            refreshMusic();
        }


        return v;
    }

    private void refreshMusic() {

        FragmentMusicSource.get(getContext()).getMusic(new FragmentMusicSource.MusicListener(){
            public void onMusicResponse(List<Music> musicList){
                //Log.i("Checking",musicList.toString());
                mMusics = musicList;
                mSwipeRefreshLayout.setRefreshing(false);
                mMusicAdapter.setItems(musicList);
            }

        });
    }

    private class MusicAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<Music> mDataSource;

        public MusicAdapter(Context context){
            mContext = context;
            mDataSource = new ArrayList<>();
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public void setItems(List<Music> musicList){
            mDataSource.clear();
            mDataSource.addAll(musicList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Music music = mDataSource.get(position);
            View rowView = mInflater.inflate(R.layout.list_item_music,parent, false);

            boolean isPlaying = mMediaPlayer.isPlaying() &&
                    mCurrentlyPlayingUrl.equals(music.getPreviewUrl());
            // Here, add code to set the play/pause button icon based on isPlaying
            mImageButton = (ImageButton) rowView.findViewById(R.id.playButton);
            if(isPlaying){

                    mImageButton.setBackgroundResource(R.mipmap.ic_launcher);
            }else{
                mImageButton.setBackgroundResource(R.mipmap.ic_launcher_round);
            }
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedAudioURL(music.getPreviewUrl());
                }
            });


            imageView = (NetworkImageView) rowView.findViewById(R.id.thumbnail);
            ImageLoader loader = FragmentMusicSource.get(getContext()).getImageLoader();
            imageView.setImageUrl(music.getArtWOrkUrl100(), loader);

            mSongTitleView = (TextView)rowView.findViewById(R.id.track);
            mSongTitleView.setText(music.getTrackName());

            mTextView = (TextView)rowView.findViewById(R.id.artist);
            mTextView.setText(music.getArtistName());

            mTextView = (TextView)rowView.findViewById(R.id.collection);
            mTextView.setText(music.getCollectionName());



            return rowView;
        }
    }







}
