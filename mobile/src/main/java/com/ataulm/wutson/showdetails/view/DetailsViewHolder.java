package com.ataulm.wutson.showdetails.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class DetailsViewHolder extends RecyclerView.ViewHolder {

    protected DetailsViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Detail detail) {
    }

}
