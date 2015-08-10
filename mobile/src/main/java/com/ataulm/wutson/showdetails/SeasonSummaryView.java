package com.ataulm.wutson.showdetails;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

public class SeasonSummaryView extends RelativeLayout {

    private ImageView posterImageView;
    private TextView seasonNumberTextView;
    private TextView episodeCountTextView;

    public SeasonSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_season_summary, this);
        posterImageView = (ImageView) findViewById(R.id.season_summary_image_poster);
        seasonNumberTextView = (TextView) findViewById(R.id.season_summary_text_season_number);
        episodeCountTextView = (TextView) findViewById(R.id.season_summary_text_episode_count);
    }

    void setPoster(String uri) {
        posterImageView.setImageBitmap(null);

        // FIXME: why is the placeholder/error purple??
        Glide.with(getContext())
                .load(uri.toString())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.ic_season_or_episode_placeholder)
                .into(posterImageView);
    }

    void setSeasonNumber(int seasonNumber) {
        if (seasonNumber == 0) {
            seasonNumberTextView.setText(R.string.show_season_summary_season_number_0);
        } else {
            String seasonNumberText = getResources().getString(R.string.show_season_summary_season_number_format, seasonNumber);
            seasonNumberTextView.setText(seasonNumberText);
        }
    }

    void setEpisodeCount(int episodeCount) {
        String episodeCountText = getResources().getString(R.string.show_season_summary_episode_count_format, episodeCount);
        episodeCountTextView.setText(episodeCountText);
    }

}
