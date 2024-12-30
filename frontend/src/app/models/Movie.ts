import { Genre } from "./Genre";


export interface Movie {
    movieId: number;
    title: string;
    releaseDate: string;
    runtime: number | null;
    overview: string;
    posterPath: string;
    backdropPath: string;
    tmdbId: number;
    genres: Genre[];
    popularity: number;
    voteAverage: number;
    voteCount: number;  
  }
  export interface Page {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
}

export interface MovieResponse {
    content: Movie[];
    page: Page;
}