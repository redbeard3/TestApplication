package com.example.myapplication.model.common;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomItemDecorator extends RecyclerView.ItemDecoration {

    private int offset;

    public CustomItemDecorator(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();

        if (params.getSpanIndex() % 2 == 0) {
            outRect.top = offset;
            outRect.left = offset;
            outRect.right = offset / 2;
        } else {
            outRect.top = offset;
            outRect.right = offset;
            outRect.left = offset / 2;
        }
    }
}
