package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;

public class BigPhotoActivity extends AbstactFragmentActivity {
    @Override
    public Fragment createFragment() {
        return PhotoFragment.newInstance(getIntent().getData());
    }

    public static Intent newIntent(Context context, Uri photoPageUri) {
        Intent intent = new Intent(context, BigPhotoActivity.class);
        intent.setData(photoPageUri);
        return intent;
    }
}
