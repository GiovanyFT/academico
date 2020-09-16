package com.example.academico.util;

import android.support.v7.widget.RecyclerView;

public abstract  class ListRecyclerView<T> extends RecyclerView.Adapter {
    public abstract T getItem(int position);
}
