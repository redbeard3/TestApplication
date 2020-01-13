package com.example.myapplication;

import androidx.fragment.app.Fragment;

public class MainActivity extends AbstactFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new AlbumListFragment().newInstance();
    }
}
