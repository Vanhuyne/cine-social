
CREATE TABLE IF NOT EXISTS votes (
    vote_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    review_id BIGINT NOT NULL,
    vote_type ENUM('UP', 'DOWN') NOT NULL,  -- Using ENUM for predefined values
    CONSTRAINT fk_vote_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_vote_review FOREIGN KEY (review_id) REFERENCES reviews(review_id),
    CONSTRAINT uc_vote UNIQUE (user_id, review_id)
);
