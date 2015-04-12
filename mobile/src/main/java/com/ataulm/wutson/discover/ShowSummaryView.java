package com.ataulm.wutson.discover;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

import java.net.URI;

public class ShowSummaryView extends FrameLayout {

    private static final float HEIGHT_BY_WIDTH_RATIO = 214f / 178;
    private static final float HALF_PIXEL = 0.5f;

    private ImageView posterImageView;
    private TextView titleTextView;

    public ShowSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = (int) (width * HEIGHT_BY_WIDTH_RATIO + HALF_PIXEL);

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_discover_show_summary, this);

        posterImageView = (ImageView) findViewById(R.id.show_summary_image_poster);
        titleTextView = (TextView) findViewById(R.id.show_summary_text_title);
    }

    public void setPoster(URI uri) {
        posterImageView.setImageBitmap(null);

        Glide.with(getContext())
                .load(uri.toString())
                .asBitmap()
                .into(posterImageView);
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

}
