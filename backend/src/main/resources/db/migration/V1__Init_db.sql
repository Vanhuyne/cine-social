-- Create movies table
CREATE TABLE movies (
    movie_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    release_date DATE,
    runtime INTEGER,
    overview TEXT,
    poster_path VARCHAR(255),
    backdrop_path VARCHAR(255),
    tmdb_id INTEGER UNIQUE,
    created_at DATETIME,
    popularity DOUBLE,
    vote_average DOUBLE,
    vote_count INTEGER,
    trailer_key VARCHAR(255)
);

-- Create genres table
CREATE TABLE genres (
    genre_id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Create junction table for movies and genres
CREATE TABLE movie_genres (
    movie_id BIGINT,
    genre_id BIGINT,
    PRIMARY KEY (movie_id, genre_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id)
);

-- Create users table
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_picture VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);

-- Create user_roles table
CREATE TABLE user_roles (
    user_id BIGINT,
    role VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Create refresh_tokens table
CREATE TABLE refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);