ALTER TABLE watchlists
DROP FOREIGN KEY watchlists_ibfk_2;

-- Now drop the movie_id column
ALTER TABLE watchlists
DROP COLUMN movie_id;

CREATE TABLE watchlist_movies (
    watchlist_id int NOT NULL,
    movie_id int NOT NULL,

    -- Foreign keys to Watchlist and Movie
    CONSTRAINT fk_watchlist_movies_watchlist FOREIGN KEY (watchlist_id) REFERENCES watchlists(watchlist_id),
    CONSTRAINT fk_watchlist_movies_movie FOREIGN KEY (movie_id) REFERENCES movies(movie_id),

    PRIMARY KEY (watchlist_id, movie_id)
);