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

//    @Override
//    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
//        canvas.save();
//
//        DividerItemDecoration divider = new DividerItemDecoration();
//
//        final int leftWithMargin = convertDpToPixel(56);
//        final int right = parent.getWidth();
//
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//            int adapterPosition = parent.getChildAdapterPosition(child);
//            left = (adapterPosition == lastPosition) ?  0 : leftWithMargin;
//            parent.getDecoratedBoundsWithMargins(child, mBounds);
//            final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
//            final int top = bottom - mDivider.getIntrinsicHeight();
//            mDivider.setBounds(left, top, right, bottom);
//            mDivider.draw(canvas);
//        }
//        canvas.restore();
//    }
}
