package com.ataulm.wutson.showdetails;

import android.util.Log;

import com.ataulm.wutson.episodes.Episodes;
import com.ataulm.wutson.model.TmdbConfiguration;
import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonCredits;
import com.ataulm.wutson.tmdb.gson.GsonTvShow;
import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import static com.ataulm.wutson.rx.Function.ignoreEmptyStrings;
import static com.ataulm.wutson.rx.Function.jsonTo;

public class ShowRepository {

    private final TmdbApi api;
    private final PersistentDataRepository persistentDataRepository;
    private final ConfigurationRepository configurationRepository;
    private final Gson gson;

    public ShowRepository(TmdbApi api, PersistentDataRepository persistentDataRepository, ConfigurationRepository configurationRepository, Gson gson) {
        this.api = api;
        this.persistentDataRepository = persistentDataRepository;
        this.configurationRepository = configurationRepository;
        this.gson = gson;
    }

    public Observable<Show> getShowDetails(String showId) {
        Observable<TmdbConfiguration> configurationObservable = configurationRepository.getConfiguration();
        Observable<GsonTvShow> gsonTvShowObservable = fetchJsonTvShowFrom(persistentDataRepository, showId)
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonTvShow.class, gson))
                .switchIfEmpty(api.getTvShow(showId).doOnNext(saveTo(persistentDataRepository, gson, showId)));

        return Observable.zip(configurationObservable, gsonTvShowObservable, asShow(showId));
    }

    private static Action1<GsonTvShow> saveTo(final PersistentDataRepository persistentDataRepository, final Gson gson, final String tmdbShowId) {
        return new Action1<GsonTvShow>() {

            @Override
            public void call(GsonTvShow gsonTvShow) {
                String json = gson.toJson(gsonTvShow, GsonTvShow.class);
                persistentDataRepository.writeJsonShowDetails(tmdbShowId, json);
            }

        };
    }

    private static Observable<String> fetchJsonTvShowFrom(final PersistentDataRepository repository, final String showId) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(repository.readJsonShowDetails(showId));
                subscriber.onCompleted();
            }

        });
    }

    private static Func2<TmdbConfiguration, GsonTvShow, Show> asShow(final String showId) {
        return new Func2<TmdbConfiguration, GsonTvShow, Show>() {

            @Override
            public Show call(TmdbConfiguration configuration, GsonTvShow gsonTvShow) {
                List<Character> characters = getCharacters(configuration, gsonTvShow);

                String name = gsonTvShow.name;
                String overview = gsonTvShow.overview;
                URI posterUri = configuration.completePoster(gsonTvShow.posterPath);
                URI backdropUri = configuration.completeBackdrop(gsonTvShow.backdropPath);
                Cast cast = new Cast(characters);

                List<Show.Season> seasons = getSeasons(configuration, gsonTvShow);
                return new Show(gsonTvShow.id, name, overview, posterUri, backdropUri, cast, seasons);
            }

            private List<Character> getCharacters(TmdbConfiguration configuration, GsonTvShow gsonTvShow) {
                List<Character> characters = new ArrayList<>();
                for (GsonCredits.Cast.Entry entry : gsonTvShow.gsonCredits.cast) {
                    Actor actor = new Actor(entry.actorName, configuration.completeProfile(entry.profilePath));
                    characters.add(new Character(entry.name, actor));
                }
                return characters;
            }

            private List<Show.Season> getSeasons(TmdbConfiguration configuration, GsonTvShow gsonTvShow) {
                List<Show.Season> seasons = new ArrayList<>();
                for (GsonTvShow.Season season : gsonTvShow.seasons) {
                    String id = season.id;
                    int seasonNumber = season.seasonNumber;
                    int episodeCount = season.episodeCount;
                    URI posterPath = configuration.completePoster(season.posterPath);
                    seasons.add(new Show.Season(id, showId, seasonNumber, episodeCount, posterPath));
                }
                return seasons;
            }

        };
    }

}
