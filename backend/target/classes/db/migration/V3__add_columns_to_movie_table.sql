-- V2__add_columns_to_movie_table.sql

ALTER TABLE movies
ADD COLUMN popularity DOUBLE PRECISION,
ADD COLUMN vote_average DOUBLE PRECISION,
ADD COLUMN vote_count INT;
