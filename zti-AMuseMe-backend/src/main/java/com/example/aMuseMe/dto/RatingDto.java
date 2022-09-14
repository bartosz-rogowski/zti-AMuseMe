package com.example.aMuseMe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Null;

@Builder
@Data
@AllArgsConstructor
public class RatingDto {
    private String albumID;

    @Null
    private Long userID;

    private int rate;
}
