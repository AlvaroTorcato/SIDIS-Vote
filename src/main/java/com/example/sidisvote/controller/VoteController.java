package com.example.sidisvote.controller;

import com.example.sidisvote.model.Vote;
import com.example.sidisvote.model.VoteDTO;
import com.example.sidisvote.model.VoteDetailsDTO;
import com.example.sidisvote.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Tag(name = "Votes", description = "Endpoints for managing votes")
@RequestMapping("/votes")
@RestController
public class VoteController {
    @Autowired
    VoteService service;
    @Operation(summary = "Make a vote in a review")
    @PostMapping(value = "/{idReview}")
    @ResponseStatus(HttpStatus.CREATED)
    public VoteDTO create(@RequestBody final VoteDetailsDTO resource, @PathVariable("idReview") final int idReview, HttpServletRequest request) throws IOException {
        return service.createVote(resource,idReview,request);
    }

    @Operation(summary = "Search for votes in review")
    @GetMapping(value = "/search/{idReview}")
    public List<VoteDTO> searchVotes(@PathVariable("idReview") final int idReview) throws IOException {
        return service.searchVotes(idReview);
    }
}
