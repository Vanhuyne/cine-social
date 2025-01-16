package com.huy.backend.repository;

import com.huy.backend.models.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchListRepo extends JpaRepository<Watchlist, Long> {

}
