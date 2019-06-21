package com.aidingyun.ynlive.mvp.ui.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aidingyun.ynlive.R;
import com.aidingyun.ynlive.mvp.ui.fragment.StandpointFragment;

public class StandpointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        StandpointFragment fragment = new StandpointFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.content_fragment, fragment);
        transaction.commit();
    }
}