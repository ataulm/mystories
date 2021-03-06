package com.ataulm.wutson.showdetails.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ataulm.wutson.DeveloperError;

public class DetailsAdapter extends RecyclerView.Adapter<DetailViewHolder> {

    private final LayoutInflater layoutInflater;

    private Details details;

    public DetailsAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void update(Details details) {
        this.details = details;
        notifyDataSetChanged();
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Type type = Type.from(viewType);
        switch (type) {
            case HEADER_SPACE:
                return HeaderSpaceDetailViewHolder.newInstance(layoutInflater, parent);
            case OVERVIEW:
                return OverviewDetailViewHolder.newInstance(layoutInflater, parent);
            case CAST_TITLE:
                return CastTitleDetailViewHolder.newInstance(layoutInflater, parent);
            case CHARACTER:
                return CharacterDetailViewHolder.newInstance(layoutInflater, parent);
            default:
                throw DeveloperError.because("no viewholder for: " + type);
        }
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        Detail detail = details.get(position);
        holder.bind(detail);
    }

    @Override
    public int getItemCount() {
        if (details == null) {
            return 0;
        }
        return details.size();
    }

    @Override
    public int getItemViewType(int position) {
        return details.getType(position).ordinal();
    }

}
