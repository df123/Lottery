package com.example.lottery.utils;

import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewUtils {
    public static RecyclerView initRecyclerView(View inflate, int id, int spanCount, FragmentActivity fragmentActivity) {
        RecyclerView recyclerView = inflate.findViewById(id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(fragmentActivity, spanCount));
        return recyclerView;
    }
}
