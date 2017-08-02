package com.example.aakashb.itunesproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager frag = getSupportFragmentManager();
        Fragment existingFragment = frag.findFragmentById(R.id.container);

        if(existingFragment == null){
            Fragment itunesMusicListFragment = new ItunesMusicListFragment();
            frag.beginTransaction().replace(R.id.container,itunesMusicListFragment).commit();

        }


    }
}
