package com.ataulm.wutson.discover;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.view.Displayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowsInGenreItemView extends FrameLayout implements Displayer<Show> {

    private final static float HEIGHT_BY_WIDTH_RATIO = 272f / 185;
    private final static float HALF_PIXEL = 0.5f;

    private ImageView posterImageView;
    private TextView nameTextView;

    public ShowsInGenreItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowsInGenreItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = (int) (width * HEIGHT_BY_WIDTH_RATIO + HALF_PIXEL);

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_shows_in_genre_item, this);

        posterImageView = (ImageView) findViewById(R.id.shows_in_genre_image_poster);
        nameTextView = (TextView) findViewById(R.id.shows_in_genre_text_name);
    }

    @Override
    public void display(final Show show) {
        posterImageView.setTag(R.id.view_tag_show_poster_uri, show.getPosterUri());
        Glide.with(getContext())
                .load(show.getPosterUri().toString())
                .asBitmap()
                .into(bitmapTarget(show));
        nameTextView.setText(show.getName());
    }

    private BitmapImageViewTarget bitmapTarget(final Show show) {
        return new BitmapImageViewTarget(posterImageView) {

            @Override
            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource.isRecycled()) {
                    return;
                }
                if (posterShouldStillDisplay(show)) {
                    posterImageView.setImageBitmap(resource);
                }

                Observable.create(generatePaletteFrom(resource))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new PaletteGenerationObserver(show));
            }

        };
    }

    private Observable.OnSubscribe<Palette> generatePaletteFrom(final Bitmap resource) {
        return new Observable.OnSubscribe<Palette>() {

            @Override
            public void call(Subscriber<? super Palette> subscriber) {
                Palette palette = Palette.generate(resource, 32);
                subscriber.onNext(palette);
                subscriber.onCompleted();
            }

        };
    }

    private class PaletteGenerationObserver implements Observer<Palette> {

        private final Show show;

        public PaletteGenerationObserver(Show show) {
            this.show = show;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Palette palette) {
            Palette.Swatch swatch = getAvailableSwatch(palette);
            if (posterShouldStillDisplay(show)) {
                nameTextView.setBackgroundColor(swatch.getRgb());
                nameTextView.setTextColor(swatch.getTitleTextColor());
            }
        }

        private Palette.Swatch getAvailableSwatch(Palette palette) {
            List<Palette.Swatch> orderedSwatches = Arrays.asList(
                    palette.getMutedSwatch(),
                    palette.getDarkMutedSwatch(),
                    palette.getDarkVibrantSwatch(),
                    palette.getVibrantSwatch()
            );
            for (Palette.Swatch swatch : orderedSwatches) {
                if (swatch != null) {
                    return swatch;
                }
            }
            return null;
        }

    }

    private boolean posterShouldStillDisplay(Show show) {
        return show.getPosterUri().equals(posterImageView.getTag(R.id.view_tag_show_poster_uri));
    }

}
