package com.example.aakashb.itunesproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentMusicSource {

    public interface MusicListener {
        void onMusicResponse(List<Music> musicList);
    }

    private static FragmentMusicSource sFragmentMusicSourceInstance;

    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private final static int IMAGE_CACHE_COUNT=50;


    public static FragmentMusicSource get(Context context){
        if(sFragmentMusicSourceInstance == null){
            sFragmentMusicSourceInstance = new FragmentMusicSource(context);
        }
        return sFragmentMusicSourceInstance;
    }

    public FragmentMusicSource(Context context) {
        mContext = context.getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(mContext);

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(IMAGE_CACHE_COUNT);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

    }


    public void getMusic(MusicListener musicListener) {
        final MusicListener musicListenerInternal = musicListener;

        String url = "https://itunes.apple.com/search?term=nirvana&entity=musicTrack";
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            List<Music> musicList = new ArrayList<Music>();
                            // Get the map of articles, keyed by article id.
                            JSONArray jsonobjectarray = response.getJSONArray("results");
                            Log.i("JSONVAI",jsonobjectarray.toString());
                            for(int i = 0; i<jsonobjectarray.length(); i++){
                                JSONObject jsonobject = jsonobjectarray.getJSONObject(i);
                                Music music = new Music(jsonobject);
                                musicList.add(music);
                            }

                            musicListenerInternal.onMusicResponse(musicList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            musicListenerInternal.onMusicResponse(null);
                            Toast.makeText(mContext, "Could not get articles.", Toast.LENGTH_SHORT);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        musicListenerInternal.onMusicResponse(null);
                        Toast.makeText(mContext, "Could not get articles.", Toast.LENGTH_SHORT);
                    }
                });

        mRequestQueue.add(jsonObjRequest);
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }


}
