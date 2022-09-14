package com.example.aMuseMe.repository;

import com.example.aMuseMe.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Collection<Rating> findRatingsByUser_id(Long userID);

    void deleteByUser_id(Long userID);

    @Query("SELECT AVG(r.rate) FROM Rating r WHERE r.albumMBID = ?1")
    Double getAvgRatingByAlbumMBID(String albumMBID);
}
