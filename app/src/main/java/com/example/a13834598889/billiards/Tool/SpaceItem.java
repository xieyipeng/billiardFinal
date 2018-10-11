package com.example.a13834598889.billiards.Tool;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xieyipeng on 2018/10/3.
 */

public class SpaceItem extends RecyclerView.ItemDecoration{

    private int space;

    public  SpaceItem(int space){
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
    }
}