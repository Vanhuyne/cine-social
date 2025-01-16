ALTER TABLE watchlists
ADD CONSTRAINT fk_watchlist_user FOREIGN KEY (user_id) REFERENCES users(user_id)