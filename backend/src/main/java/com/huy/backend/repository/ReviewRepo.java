package com.huy.backend.repository;

import com.huy.backend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findByMovie_MovieId(Long movieId);

    Optional<Review> findByUser_UserIdAndMovie_MovieId(Long userId, Long movieId);

//    List<Review> findByUser(User user);
//
//    Review findByUserAndMovie(User user, Movie movie);
//
//    void deleteByUserAndMovie(User user, Movie movie);
}
