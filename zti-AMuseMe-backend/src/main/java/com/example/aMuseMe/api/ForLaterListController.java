package com.example.aMuseMe.api;

import com.example.aMuseMe.dto.ForLaterListDto;
import com.example.aMuseMe.model.ForLaterList;
import com.example.aMuseMe.service.ForLaterListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(path = "api/list")
public class ForLaterListController {
    private ForLaterListService forLaterListService;

    @Autowired
    public ForLaterListController(ForLaterListService forLaterListService){
        this.forLaterListService = forLaterListService;
    }

    @GetMapping(path = "{userID}")
    public Collection<ForLaterList> getRecordsByUser(@PathVariable("userID") Long userID) {
        return forLaterListService.getRecordsByUser(userID);
    }

    @PostMapping
    public ResponseEntity<?> addRecord(@RequestBody ForLaterListDto forLaterListDto) {
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/list").toUriString()
        );
        return ResponseEntity.created(uri).body(forLaterListService.addRecord(forLaterListDto));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable("id") Long id) {
        forLaterListService.deleteRecord(id);
        return ResponseEntity.ok().build();
    }
}
