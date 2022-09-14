package com.example.aMuseMe.api;


import com.example.aMuseMe.dto.ForLaterListDto;
import com.example.aMuseMe.dto.RatingDto;
import com.example.aMuseMe.dto.RatingWithIdDto;
import com.example.aMuseMe.model.ForLaterList;
import com.example.aMuseMe.model.Rating;
import com.example.aMuseMe.service.ForLaterListService;
import com.example.aMuseMe.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(path = "api/rate")
public class RatingController {
    private RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService){
        this.ratingService = ratingService;
    }

    @GetMapping(path = "{userID}")
    public Collection<Rating> getRatingsByUser(@PathVariable("userID") Long userID) {
        return ratingService.getRatingsByUser(userID);
    }

    @PostMapping
    public ResponseEntity<?> addRating(@RequestBody RatingDto ratingDto) {
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/rate").toUriString()
        );
        return ResponseEntity.created(uri).body(ratingService.addRating(ratingDto));
    }

    @PutMapping
    public ResponseEntity<?> changeRating(@RequestBody RatingWithIdDto ratingDto) {
        ratingService.updateRating(ratingDto.getId(), ratingDto.getRate());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteRating(@PathVariable("id") Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok().build();
    }
}
