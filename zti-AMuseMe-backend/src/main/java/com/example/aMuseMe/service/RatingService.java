package com.example.aMuseMe.service;

import com.example.aMuseMe.dto.RatingDto;
import com.example.aMuseMe.exception.ApiRequestException;
import com.example.aMuseMe.model.Rating;
import com.example.aMuseMe.model.User;
import com.example.aMuseMe.repository.RatingRepository;
import com.example.aMuseMe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    public Rating addRating(RatingDto ratingDto) {
        Rating rating = new Rating();
        Optional<User> userOptional = userRepository.findById(ratingDto.getUserID());
        if (userOptional.isPresent()) {
            rating.setUser(userOptional.get());
        }
        else {
            throw new ApiRequestException("User not found");
        }
        rating.setAlbumMBID(ratingDto.getAlbumID());
        rating.setRate(ratingDto.getRate());
        rating.setDate(LocalDate.now());
        return ratingRepository.save(rating);
    }

    public Rating getRatingByID(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(
                        () -> new ApiRequestException("There is no user with given ID")
                );
        return rating;
    }

    public Collection<Rating> getRatingsByUser(Long userID) {
        Optional<User> userOptional = userRepository.findById(userID);
        if (userOptional.isPresent()) {
            return ratingRepository.findRatingsByUser_id(userID);
        }
        else {
            throw new ApiRequestException("User not found");
        }
    }

    @Transactional
    public void updateRating(
            Long id,
            int rate
    ) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(
                        () -> new ApiRequestException("There is no such rating")
                );
        if (rate >= 0 && rate <= 5
                && !Objects.equals(rating.getRate(), rate)
        ) {
            rating.setRate(rate);
            rating.setDate(LocalDate.now());
        }
        else {
            throw new ApiRequestException("Rate must be between 0 and 5");
        }
    }

    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }
}
