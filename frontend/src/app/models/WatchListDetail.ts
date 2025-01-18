import { Movie } from "./Movie";

export interface WatchListDetail {
    watchlistId: number;
    name: string;
    movies: Movie[];
}