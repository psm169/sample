package com.bignerdranch.android.memo.Categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bignerdranch.android.memo.R;

/**
 * Created by realbyte on 2017. 7. 17..
 */

public class MemokindCategoriActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_categori_kind_list);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer5);

        if(fragment == null)
        {
            fragment = new MemokindCategoriFragment();
            fm.beginTransaction().add(R.id.fragmentContainer5,fragment).commit();
        }

    }
}
