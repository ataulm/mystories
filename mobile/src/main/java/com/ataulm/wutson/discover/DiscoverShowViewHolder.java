package com.ataulm.wutson.discover;

import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.ShowSummary;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

class DiscoverShowViewHolder extends RecyclerView.ViewHolder {

    private final View itemView;
    private final TextView nameTextView;
    private final ImageView posterImageView;
    private final View overlay;

    @ColorInt
    private final int defaultOverlayBackgroundColor;

    private final DiscoverShowSummaryInteractionListener listener;

    static DiscoverShowViewHolder inflate(LayoutInflater layoutInflater, ViewGroup parent, DiscoverShowSummaryInteractionListener listener) {
        View view = layoutInflater.inflate(R.layout.view_discover_show, parent, false);

        TextView nameTextView = (TextView) view.findViewById(R.id.discover_show_text_name);
        ImageView posterImageView = (ImageView) view.findViewById(R.id.discover_show_image_poster);
        View overlay = view.findViewById(R.id.discover_show_overlay);

        int defaultOverlayBackgroundColor = parent.getResources().getColor(R.color.discover_show_overlay_background);
        return new DiscoverShowViewHolder(view, nameTextView, posterImageView, overlay, defaultOverlayBackgroundColor, listener);
    }

    private DiscoverShowViewHolder(View itemView, TextView nameTextView, ImageView posterImageView, View overlay,
                                   @ColorInt int defaultOverlayBackgroundColor,
                                   DiscoverShowSummaryInteractionListener listener) {
        super(itemView);
        this.itemView = itemView;
        this.nameTextView = nameTextView;
        this.posterImageView = posterImageView;
        this.overlay = overlay;

        this.defaultOverlayBackgroundColor = defaultOverlayBackgroundColor;
        this.listener = listener;
    }

    public void bind(final ShowSummary showSummary) {
        nameTextView.setText(showSummary.getName());
        loadImage(showSummary);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(showSummary);
            }
        });
    }

    private void loadImage(final ShowSummary showSummary) {
        overlay.setBackgroundColor(defaultOverlayBackgroundColor);
        overlay.setTag(R.id.discover_show_view_tag, showSummary.getId());

        posterImageView.setImageBitmap(null);
        Glide.with(posterImageView.getContext())
                .load(showSummary.getPosterUri().toString())
                .asBitmap()
                .placeholder(R.drawable.ic_brand_placeholder)
                .into(
                        new BitmapImageViewTarget(posterImageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                super.setResource(resource);
                                Palette.from(resource).generate(new OnPaletteGeneratedListener(showSummary.getId()));
                            }
                        }
                );
    }

    private class OnPaletteGeneratedListener implements Palette.PaletteAsyncListener {

        private final ShowId id;

        public OnPaletteGeneratedListener(ShowId id) {
            this.id = id;
        }

        @Override
        public void onGenerated(Palette palette) {
            if (showIdHasChanged()) {
                return;
            }
            int fallbackColor = defaultOverlayBackgroundColor;
            int generatedColor = palette.getDarkMutedColor(fallbackColor);
            overlay.setBackgroundColor(generatedColor);
        }

        private boolean showIdHasChanged() {
            return !id.equals(overlay.getTag(R.id.discover_show_view_tag));
        }

    }

}
