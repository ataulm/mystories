package com.ataulm.wutson.tmdb;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface TmdbApi {

    @GET("/tv/{id}")
    Observable<TmdbTvShow> getShow(@Path("id") String id);

}
