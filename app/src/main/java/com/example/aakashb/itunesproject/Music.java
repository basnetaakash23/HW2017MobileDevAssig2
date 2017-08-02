package com.example.aakashb.itunesproject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aakashb on 8/1/17.
 */

public class Music {
    protected String mKind;
    protected String mTrackName;
    protected String mArtistName;
    protected String mCollectionName;
    protected String mPreviewUrl;
    protected String mArtWorkUrl60;
    protected String mTrackViewUrl;

    public Music(JSONObject musicObject) {
        try{

            //ignoring music video is not done
            mTrackName = musicObject.getString("trackName");
            mArtistName = musicObject.getString("artistName");
            mCollectionName = musicObject.getString("collectionName");
            mArtWorkUrl60 = musicObject.getString("artWorkUrl60");
            mTrackViewUrl = musicObject.getString("trackViewUrl");
            mPreviewUrl = musicObject.getString("previewUrl");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getURLString() {
        return mTrackViewUrl;

    }

    public String getTrackName(){
        return mTrackName;
    }

    public String getArtistName(){
        return mArtistName;
    }

    public String getCollectionName(){
        return mCollectionName;
    }

    public String getArtWOrkUrl60(){
        return mArtWorkUrl60;
    }

    public String getTrackViewUrl(){
        return mTrackViewUrl;
    }

    public String getPreviewUrl(){
        return mPreviewUrl;
    }
}
