package com.example.myapplication;

import androidx.fragment.app.Fragment;

public class AlbumDetailActivity extends AbstactFragmentActivity {

    public static final String EXTRA_ALBUM_ID = "EXTRA_ALBUM_ID";

    @Override
    public Fragment createFragment() {
        return new AlbumDetailFragment().newInstance();
    }
}
