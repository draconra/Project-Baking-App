package com.draconra.bakingapp.util.helper;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.draconra.bakingapp.view.MainActivity;

/**
 * Created by draconra on 8/26/17.
 */

public class RecycleViewTypeHelper {

    public enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


    public static void setRecyclerViewLayoutManager(Context context, LayoutManagerType layoutManagerType, RecyclerView recyclerView, int spanCount) {
        int scrollPosition = 0;
        LinearLayoutManager layoutManager;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                layoutManager = new GridLayoutManager(context, spanCount);
                break;
            case LINEAR_LAYOUT_MANAGER:
                layoutManager = new LinearLayoutManager(context);
                break;
            default:
                layoutManager = new LinearLayoutManager(context);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }
}
