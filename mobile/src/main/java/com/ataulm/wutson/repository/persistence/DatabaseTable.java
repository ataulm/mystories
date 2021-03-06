package com.ataulm.wutson.repository.persistence;

import android.net.Uri;

import java.util.Locale;

enum DatabaseTable {

    TRENDING_SHOWS,
    POPULAR_SHOWS,
    TRACKED_SHOWS,
    SHOW_DETAILS,
    SHOW_PEOPLE,
    SEASONS;

    Uri uri() {
        return Uri.parse(WutsonSQLiteContentProvider.AUTHORITY).buildUpon().appendPath(tableName()).build();
    }

    private String tableName() {
        return name().toLowerCase(Locale.UK);
    }

}
