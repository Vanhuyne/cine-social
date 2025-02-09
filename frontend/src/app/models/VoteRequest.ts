export interface VoteRequest {
    reviewId: number;
    voteType: 'UP' | 'DOWN';
}