package com.example.hp.movietv;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class CelebActivity extends AppCompatActivity {
    RecyclerView popularRecycler;
    CelebAdapterVertical celebAdapter;
     ArrayList<CelebResponse.Celeb> celebArrayList;
     ConstraintLayout constraintLayout;
     ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celeb);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


}
