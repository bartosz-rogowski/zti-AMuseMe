package com.example.aMuseMe.service;

import com.example.aMuseMe.dto.ForLaterListDto;
import com.example.aMuseMe.exception.ApiRequestException;
import com.example.aMuseMe.model.ForLaterList;
import com.example.aMuseMe.model.User;
import com.example.aMuseMe.repository.ForLaterListRepository;
import com.example.aMuseMe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ForLaterListService {

    private final ForLaterListRepository forLaterListRepository;
    private final UserRepository userRepository;

    public ForLaterList addRecord(ForLaterListDto forLaterListDto) {
        ForLaterList forLaterList = new ForLaterList();
        Optional<User> userOptional = userRepository.findById(forLaterListDto.getUserID());
        if (userOptional.isPresent()) {
            forLaterList.setUser(userOptional.get());
        }
        else {
            throw new ApiRequestException("User not found");
        }
        forLaterList.setAlbumMBID(forLaterListDto.getAlbumID());
        return forLaterListRepository.save(forLaterList);
    }

    public List<ForLaterList> getRecords() {
        return forLaterListRepository.findAll();
    }

    public Collection<ForLaterList> getRecordsByUser(Long userID) {
        Optional<User> userOptional = userRepository.findById(userID);
        if (userOptional.isPresent()) {
            return forLaterListRepository.findRecordsByUser_id(userID);
        }
        else {
            throw new ApiRequestException("User not found");
        }
    }

    public void deleteRecord(Long id) {
        forLaterListRepository.deleteById(id);
    }
}
