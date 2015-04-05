package com.ataulm.wutson.seasons;

import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;
import com.ataulm.wutson.vpa.ViewPagerAdapter;

class SeasonsPagerAdapter extends ViewPagerAdapter {

    private final Seasons seasons;
    private final LayoutInflater layoutInflater;
    private final Resources resources;

    SeasonsPagerAdapter(Seasons seasons, LayoutInflater layoutInflater, Resources resources) {
        this.seasons = seasons;
        this.layoutInflater = layoutInflater;
        this.resources = resources;
    }

    @Override
    protected View getView(ViewGroup container, int position) {
        RecyclerView view = (RecyclerView) layoutInflater.inflate(R.layout.view_season_page, container, false);
        view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        RecyclerView.Adapter adapter = new SeasonAdapter(seasons.get(position), layoutInflater);
        adapter.setHasStableIds(true);
        view.setAdapter(adapter);
        return view;
    }

    @Override
    public int getCount() {
        return seasons.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            // FIXME: dat direct usage without aliases - also, this isn't true for shows without Specials!
            return resources.getString(R.string.specials);
        } else {
            Season season = seasons.get(position);
            // TODO: dat literal
            return "SEASON " + season.getSeasonNumber();
        }
    }

}
