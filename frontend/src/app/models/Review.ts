export interface ReviewRequest {
    movieId: number;
    rating: number;
    comment: string;
}
export interface ReviewResponse{
    reviewId: number;
    userDisplayName: string;
    userAvatar?: string;
    rating: number;
    comment: string;
    createdAt: string;
    updatedAt: string;
    upVotes : number;
    downVotes : number;
    userVote?: 'UP' | 'DOWN' | null;
}

export interface ReviewRecentResponse {
    reviewId: number;
    userDisplayName: string;
    userAvatar?: string;
    rating: number;
    comment: string;
    createdAt: string;
    totalVotes: number;
    movie : MovieInfo;
}

export interface MovieInfo {
    movieId: number;
    title: string;
    // poster: string;
}
export interface PageResponse<T> {
    content: T[];
    page: {
      size: number;
      number: number;
      totalElements: number;
      totalPages: number;
    };
  }