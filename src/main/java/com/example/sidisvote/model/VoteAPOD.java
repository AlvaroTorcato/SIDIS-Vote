package com.example.sidisvote.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VoteAPOD {
    private int id;
    private boolean vote;
    private int reviewId;
    private int userId;

    public VoteAPOD(@JsonProperty("id") int id,
                    @JsonProperty("vote") boolean vote,
                    @JsonProperty("reviewId") int reviewId,
                    @JsonProperty("userId") int userId) {
        this.id = id;
        this.vote = vote;
        this.reviewId = reviewId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public boolean isVote() {
        return vote;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getUserId() {
        return userId;
    }
}
