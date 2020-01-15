package com.example.myapplication.model.common;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CustomItemDecorator extends RecyclerView.ItemDecoration {

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int count = parent.getChildCount();
        int width = parent.getWidth();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            int bottom = child.getBottom();
            c.drawRect(0, bottom, width, bottom + 2, new Paint());
        }
    }

}
