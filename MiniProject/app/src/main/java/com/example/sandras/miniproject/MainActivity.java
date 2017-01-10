package com.example.sandras.miniproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    Progress mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgress = (Progress) findViewById(R.id.progress);

        //add items to carousel
        Carousel c = (Carousel) findViewById(R.id.carousel);
        loadList(c);
        c.setOnSelectListener(new Carousel.OnSelectListener() {
            @Override
            public void onSelect(int i) {
                Log.d("Main", "select: " + i);
                mProgress.setCurrent(i + 1);
            }
        });

        c.setImageHeight(400);
        c.setImagePadding(100);
        c.removeFromList(5);
        mProgress.setNumOfSteps(c.getNumOfItems());
    }

    private void loadList(Carousel c) {
        c.addToList(R.drawable.twopm);
        c.addToList(R.drawable.apink);
        c.addToList(R.drawable.bap);
        c.addToList(R.drawable.bigbang);
        c.addToList(R.drawable.bts);
        c.addToList(R.drawable.exo);
        c.addToList(R.drawable.fx);
        c.addToList(R.drawable.gg);
        c.addToList(R.drawable.got7);
        c.addToList(R.drawable.secret);
        c.addToList(R.drawable.shinee);
        c.addToList(R.drawable.sistar);
        c.addToList(R.drawable.wondergirls);
    }
}
