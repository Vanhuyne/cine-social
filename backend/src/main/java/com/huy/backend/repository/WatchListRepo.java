package com.huy.backend.repository;

import com.huy.backend.models.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchListRepo extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUser_UserId(Long userId);

}
