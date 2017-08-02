package com.example.aakashb.itunesproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem menu){
        final EditText mEditText = new EditText(this);

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Search for the tracks")
                .setView(mEditText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String query = mEditText.getText().toString();
                        FragmentManager frag = getSupportFragmentManager();
                        Fragment itunesMusicListFragment = new ItunesMusicListFragment();
                        FragmentMusicSource.setQuery(query);
                        frag.beginTransaction().replace(R.id.container, itunesMusicListFragment).commit();
                    }
                })
                .setNegativeButton(android.R.string.cancel,null).create();

        alertDialog.show();
        return true;



    }



    

        /*
        FragmentManager frag = getSupportFragmentManager();
        Fragment itunesMusicListFragment = new ItunesMusicListFragment();
        Log.d("AB", "Hi");
        frag.beginTransaction().replace(R.id.container,itunesMusicListFragment).commit();*/
}
