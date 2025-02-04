export interface ReviewRequest {
    movieId: number;
    rating: number;
    comment: string;
}
export interface Review{
    reviewId: number;
    userDisplayName: string;
    rating: number;
    comment: string;
    createdAt: string;
    updatedAt: string;
}