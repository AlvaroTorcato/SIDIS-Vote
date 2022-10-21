package com.example.sidisvote.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class VoteDetailsDTO {
    private boolean vote;
    @JsonCreator
    public VoteDetailsDTO(@JsonProperty("vote") boolean vote) {
        this.vote = vote;
    }

    public boolean getVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }
}
