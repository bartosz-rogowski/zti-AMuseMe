package com.example.aMuseMe.repository;

import com.example.aMuseMe.model.ForLaterList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ForLaterListRepository
        extends JpaRepository<ForLaterList, Long> {

    Collection<ForLaterList> findRecordsByUser_id(Long userID);

    void deleteByUser_id(Long userID);
}
