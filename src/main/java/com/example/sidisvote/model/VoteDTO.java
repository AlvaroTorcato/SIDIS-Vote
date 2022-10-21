package com.example.sidisvote.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class VoteDTO {

    private int id;

    private boolean vote;


    private int reviewId;

    private int userId;

    public VoteDTO(boolean vote, int reviewId, int userId) {
        this.vote = vote;
        this.reviewId = reviewId;
        this.userId = userId;
    }

    public VoteDTO(Vote vote) {
        this.id = vote.getId();
        this.vote = vote.isVote();
        this.reviewId = vote.getReviewId();
        this.userId = vote.getUserId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
