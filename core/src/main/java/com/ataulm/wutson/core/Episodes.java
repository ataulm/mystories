package com.ataulm.wutson.core;

import com.ataulm.wutson.core.episodes.Episode;

import java.util.Collections;
import java.util.List;

public class Episodes {

    private final List<Episode> episodes;

    public Episodes(List<Episode> episodes) {
        this.episodes = Collections.unmodifiableList(episodes);
    }

    public int size() {
        return episodes.size();
    }

    public Episode get(int location) {
        return episodes.get(location);
    }

}
