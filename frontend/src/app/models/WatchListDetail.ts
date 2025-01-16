export interface WatchListDetail {
    watchlistId: number;
    name: string;
    userId: number;
    movies: MovieWatchList[];
    createdAt: string;
}

export interface MovieWatchList {
    movieId: number;
    title: string;
    posterPath: string;
    releaseDate: string;
    runtime: number | null;
}
